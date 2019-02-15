/*
 * Copyright (c) 2011-2019, simple-http committers
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

package simplehttp.apache;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import org.junit.*;
import simplehttp.HttpClient;

import java.net.URL;
import java.util.Base64;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.not;
import static simplehttp.HttpClients.anApacheClient;
import static simplehttp.Url.url;
import static simplehttp.configuration.BasicAuthCredentials.basicAuth;
import static simplehttp.configuration.Password.password;
import static simplehttp.configuration.Proxy.proxy;
import static simplehttp.configuration.Username.username;
import static simplehttp.matchers.Matchers.has;
import static simplehttp.matchers.Matchers.status;
import static simplehttp.wiremock.WireMockHeaderMatcher.header;

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
    public void basicAuthorisationHeaderIsSet() {
        HttpClient http = anApacheClient().with(basicAuth(username("username"), password("password"), url("http://localhost:8080")));
        http.get(url("http://localhost:8080/test"));
        verify(getRequestedFor(urlEqualTo("/test")).withHeader("Authorization", containing("Basic " + encode("username", "password"))));
    }

    @Test
    public void basicAuthorisationHeaderIsNotSetForDifferingUrl() {
        HttpClient http = anApacheClient().with(basicAuth(username("username"), password("password"), url("https://localhost:8080")));
        http.get(url("http://localhost:8080/test"));
        verifyNoHeadersFor(urlEqualTo("/test"));
    }

    @Test
    public void basicAuthorisationForMultipleCredentials() {
        HttpClient http = anApacheClient()
            .with(basicAuth(username("username"), password("password"), url("http://localhost:8080")))
            .with(basicAuth(username("anotherUsername"), password("anotherPassword"), url("https://localhost:8080")));
        http.get(url("http://localhost:8080/test"));
        verify(getRequestedFor(urlEqualTo("/test")).withHeader("Authorization", containing("Basic " + encode("anotherUsername", "anotherPassword"))));
    }

    @Test
    @Ignore("requires manually setting up a proxy like Charles on port 8888 before running")
    public void manuallyRunThisTestWithAProxyRunningToVerifyTheHeaders() {
        URL badrobot    = url("http://baddotrobot.com");
        URL robotooling = url("http://robotooling.com");

        HttpClient http = anApacheClient()
            .with(proxy(url("http://localhost:8888")))
            .with(basicAuth(username("username"), password("password"), badrobot))
            .with(basicAuth(username("anotherUsername"), password("anotherPassword"), robotooling));
        assertThat(http.get(url("http://baddotrobot.com")), has(status(200)));
        assertThat(http.get(url("http://robotooling.com")), has(status(200)));
    }

    private void verifyNoHeadersFor(UrlPattern url) {
        List<LoggedRequest> requests = WireMock.findAll(getRequestedFor(url));
        assertThat(requests, not(contains(header("Authorization"))));
    }

    private String encode(String username, String password) {
        return Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    }

}
