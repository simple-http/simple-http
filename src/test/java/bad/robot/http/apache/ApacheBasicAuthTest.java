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
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.UrlMatchingStrategy;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import sun.misc.BASE64Encoder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static bad.robot.http.Credentials.credentials;
import static bad.robot.http.HttpClients.anApacheClient;
import static bad.robot.http.Password.password;
import static bad.robot.http.Username.username;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.not;

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
    public void basicAuthorisationHeaderIsNotSetForDifferingScheme() throws MalformedURLException {
        http.withBasicAuth(new URL("http://localhost:8080"));
        http.get(new URL("https://localhost:8080/test"));
        verifyNoHeadersFor(urlEqualTo("/test"));
    }

    private void verifyNoHeadersFor(UrlMatchingStrategy url) {
        List<LoggedRequest> requests = findAll(getRequestedFor(url));
        assertThat(requests, not(contains(HeaderMatcher.header("Authorization"))));
    }

    private String encode(String username, String password) {
        return new BASE64Encoder().encode((username + ":" + password).getBytes());
    }

    private static class HeaderMatcher extends TypeSafeMatcher<LoggedRequest> {
        private final String header;

        private HeaderMatcher(String header) {
            this.header = header;
        }

        public static HeaderMatcher header(String header) {
            return new HeaderMatcher(header);
        }

        @Override
        protected boolean matchesSafely(LoggedRequest actual) {
            return actual.containsHeader(header);
        }

        @Override
        public void describeTo(Description description) {
            description.appendValue(header);
        }
    }
}
