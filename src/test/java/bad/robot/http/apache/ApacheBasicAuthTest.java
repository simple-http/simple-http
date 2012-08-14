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

import bad.robot.http.CommonHttpClient;
import bad.robot.http.Credentials;
import bad.robot.http.Credentials;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.UrlMatchingStrategy;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.mapping.RequestPattern;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import sun.misc.BASE64Encoder;

import java.net.MalformedURLException;
import java.net.URL;

import static bad.robot.http.Credentials.credentials;
import static bad.robot.http.HttpClients.anApacheClient;
import static bad.robot.http.Password.*;
import static bad.robot.http.Username.*;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class ApacheBasicAuthTest {

    private final static WireMockServer server = new WireMockServer();

    private final CommonHttpClient http = anApacheClient().with(credentials(username("username"), password("password")));

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
        http.withBasicAuth(new URL("http://localhost:8080"));
        http.get(new URL("http://localhost:8080/test"));
        verify(getRequestedFor(urlEqualTo("/test")).withHeader("Authorization", containing("Basic " + encode("username", "password"))));
    }

    @Test
    public void basicAuthorisationHeaderIsNotSetForHost() throws MalformedURLException {
        http.withBasicAuth(new URL("https://localhost:8080"));
        http.get(new URL("http://localhost:8080/test"));
        verifyNoHeadersFor(urlEqualTo("/test"));
        verify(getRequestedFor(urlEqualTo("/test")).withHeader("Authorization", containing("Basic " + encode("username", "password"))));
    }

    // see Issue 6 of wiremock
    private void verifyNoHeadersFor(UrlMatchingStrategy url) {
        RequestPattern request = getRequestedFor(url).build();
        verify(getRequestedFor(url));
        assertThat(request.getHeaders(), is(nullValue()));
    }

    private String encode(String username, String password) {
        return new BASE64Encoder().encode((username + ":" + password).getBytes());
    }

}
