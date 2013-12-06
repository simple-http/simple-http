/*
 * Copyright (c) 2011-2013, bad robot (london) ltd
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

package bad.robot.http;

import bad.robot.http.configuration.AuthorisationCredentials;
import bad.robot.http.configuration.Proxy;
import bad.robot.http.matchers.Matchers;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Ignore;
import org.junit.Test;

import java.net.URL;

import static bad.robot.http.HeaderList.headers;
import static bad.robot.http.HeaderPair.header;
import static bad.robot.http.HttpClients.anApacheClient;
import static bad.robot.http.configuration.AccessToken.accessToken;
import static bad.robot.http.configuration.BasicAuthCredentials.basicAuth;
import static bad.robot.http.configuration.OAuthCredentials.oAuth;
import static bad.robot.http.configuration.Password.password;
import static bad.robot.http.configuration.SystemPropertyProxy.systemPropertyProxy;
import static bad.robot.http.configuration.Username.username;
import static bad.robot.http.matchers.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class HttpClientExamples {

    @Test
    public void exampleGet() {
        HttpResponse response = anApacheClient().with(systemPropertyProxy()).get(Url.url("http://www.baddotrobot.com"));
        assertThat(response, has(status(200)));
    }

    @Test
    public void exampleGetSettingHeaders() {
        HttpResponse response = anApacheClient().with(systemPropertyProxy()).get(Url.url("http://www.baddotrobot.com"),
            headers(
                header("Accept", "application/json")
            ));
        assertThat(response, has(status(200)));
    }

    @Test
    public void exampleBasicAuth() {
        AuthorisationCredentials credentials = basicAuth(username("username"), password("secret"), Url.url("http://www.baddotrobot.com"));
        HttpResponse response = anApacheClient()
            .with(systemPropertyProxy())
            .with(credentials)
            .get(Url.url("http://www.baddotrobot.com"));
        assertThat(response, has(status(200)));
    }

    @Test
    public void exampleOAuthToken() {
        AuthorisationCredentials credentials = oAuth(accessToken("XystZ5ee"), Url.url("http://www.baddotrobot.com"));
        HttpResponse response = anApacheClient().with(systemPropertyProxy()).with(credentials).get(Url.url("http://www.baddotrobot.com"));
        assertThat(response, has(status(200)));
    }

    @Test
    public void exampleMixedAuthorisationSchemes() {
        AuthorisationCredentials basicAuthCredentials = basicAuth(username("username"), password("secret"), Url.url("http://robotooling.com"));
        AuthorisationCredentials oAuthCredentials = oAuth(accessToken("XystZ5ee"), Url.url("http://baddotrobot.com"));
        HttpClient http = anApacheClient().with(systemPropertyProxy()).with(basicAuthCredentials).with(oAuthCredentials);
        http.get(Url.url("http://baddotrobot.com"));
        http.get(Url.url("http://robotooling.com"));
    }

    @Test
    @Ignore ("example using a proxy, you need to have a running proxy for this to actually run")
    public void exampleProxy() {
        anApacheClient().with(Proxy.proxy(Url.url("http://localhost:8888"))).get(Url.url("http://www.baddotrobot.com"));
    }

    @Test
    public void exampleOfDisguisingTheUserAgent() {
        anApacheClient().with(systemPropertyProxy()).get(Url.url("http://baddotrobot.com"), headers(UserAgent.SafariOnMac));
    }

    @Test
    @Ignore ("illistrative only; shows how to use a matcher")
    public void exampleOfHeaderMatcher() {
        final JUnit4Mockery context = new JUnit4Mockery();
        final HttpClient http = context.mock(HttpClient.class);
        context.checking(new Expectations() {{
            oneOf(http).get(with(any(URL.class)), with(hasHeaders(Matchers.header("User-Agent", containsString("Safari")))));
        }});
        context.assertIsSatisfied();
    }

}
