/*
 * Copyright (c) 2011-2012, bad robot (london) ltd
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package bad.robot.http.apache;

import bad.robot.http.HttpClient;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.UrlMatchingStrategy;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import org.junit.*;
import sun.misc.BASE64Encoder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static bad.robot.http.HttpClients.anApacheClient;
import static bad.robot.http.configuration.BasicAuthCredentials.basicAuth;
import static bad.robot.http.configuration.Password.password;
import static bad.robot.http.configuration.Proxy.proxy;
import static bad.robot.http.configuration.Username.username;
import static bad.robot.http.matchers.Matchers.has;
import static bad.robot.http.matchers.Matchers.status;
import static bad.robot.http.wiremock.WireMockHeaderMatcher.header;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.not;

public class ApacheBasicAuthTest {

    private final static WireMockServer server = new WireMockServer();

    @BeforeClass
    public static void startHttpServer() {
        WireMock.configureFor("localhost", 8080);
        server.start();
    }

    @AfterClass
    public static void stopHttpServer() {
        server.stop();
    }

    @Before
    public void resetHttpExpectations() {
        WireMock.reset();
    }

    @Test
    public void basicAuthorisationHeaderIsSet() throws MalformedURLException {
        HttpClient http = anApacheClient().with(basicAuth(username("username"), password("password"), new URL("http://localhost:8080")));
        http.get(new URL("http://localhost:8080/test"));
        verify(getRequestedFor(urlEqualTo("/test")).withHeader("Authorization", containing("Basic " + encode("username", "password"))));
    }

    @Test
    public void basicAuthorisationHeaderIsNotSetForDifferingUrl() throws MalformedURLException {
        HttpClient http = anApacheClient().with(basicAuth(username("username"), password("password"), new URL("https://localhost:8080")));
        http.get(new URL("http://localhost:8080/test"));
        verifyNoHeadersFor(urlEqualTo("/test"));
    }

    @Test
    public void basicAuthorisationForMultipleCredentials() throws MalformedURLException {
        HttpClient http = anApacheClient()
            .with(basicAuth(username("username"), password("password"), new URL("http://localhost:8080")))
            .with(basicAuth(username("anotherUsername"), password("anotherPassword"), new URL("https://localhost:8080")));
        http.get(new URL("http://localhost:8080/test"));
        verify(getRequestedFor(urlEqualTo("/test")).withHeader("Authorization", containing("Basic " + encode("anotherUsername", "anotherPassword"))));
    }

    @Test
    @Ignore("requires manually setting up a proxy like Charles on port 8888 before running")
    public void manuallyRunThisTestWithAProxyRunningToVerifyTheHeaders() throws MalformedURLException {
        URL badrobot    = new URL("http://baddotrobot.com");
        URL robotooling = new URL("http://robotooling.com");

        HttpClient http = anApacheClient()
            .with(proxy(new URL("http://localhost:8888")))
            .with(basicAuth(username("username"), password("password"), badrobot))
            .with(basicAuth(username("anotherUsername"), password("anotherPassword"), robotooling));
        assertThat(http.get(new URL("http://baddotrobot.com")), has(status(200)));
        assertThat(http.get(new URL("http://robotooling.com")), has(status(200)));
    }

    private void verifyNoHeadersFor(UrlMatchingStrategy url) {
        List<LoggedRequest> requests = WireMock.findAll(getRequestedFor(url));
        assertThat(requests, not(contains(header("Authorization"))));
    }

    private String encode(String username, String password) {
        return new BASE64Encoder().encode((username + ":" + password).getBytes());
    }

}
