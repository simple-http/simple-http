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

package simplehttp;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Ignore;
import org.junit.Test;
import simplehttp.configuration.AuthorisationCredentials;
import simplehttp.configuration.Proxy;
import simplehttp.matchers.Matchers;

import java.net.URL;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static simplehttp.HeaderList.headers;
import static simplehttp.HeaderPair.header;
import static simplehttp.HttpClients.anApacheClient;
import static simplehttp.configuration.AccessToken.accessToken;
import static simplehttp.configuration.BasicAuthCredentials.basicAuth;
import static simplehttp.configuration.OAuthCredentials.oAuth;
import static simplehttp.configuration.Password.password;
import static simplehttp.configuration.SystemPropertyProxy.systemPropertyProxy;
import static simplehttp.configuration.Username.username;
import static simplehttp.matchers.Matchers.*;

public class HttpClientExamples {

    private CommonHttpClient client = systemPropertyProxy().map(proxy -> anApacheClient().with(proxy))
            .orElse(anApacheClient());

    @Test
    public void exampleGet() {
        HttpResponse response = client.get(Url.url("http://www.baddotrobot.com"));
        assertThat(response, has(status(200)));
    }

    @Test
    public void exampleGetSettingHeaders() {
        HttpResponse response = client.get(Url.url("http://www.baddotrobot.com"),
            headers(
                header("Accept", "application/json")
            ));
        assertThat(response, has(status(200)));
    }

    @Test
    public void exampleBasicAuth() {
        AuthorisationCredentials credentials = basicAuth(username("username"), password("secret"), Url.url("http://www.baddotrobot.com"));
        HttpResponse response = client
            .with(credentials)
            .get(Url.url("http://www.baddotrobot.com"));
        assertThat(response, has(status(200)));
    }

    @Test
    public void exampleOAuthToken() {
        AuthorisationCredentials credentials = oAuth(accessToken("XystZ5ee"), Url.url("http://www.baddotrobot.com"));
        HttpResponse response = client.with(credentials).get(Url.url("http://www.baddotrobot.com"));
        assertThat(response, has(status(200)));
    }

    @Test
    public void exampleMixedAuthorisationSchemes() {
        AuthorisationCredentials basicAuthCredentials = basicAuth(username("username"), password("secret"), Url.url("http://robotooling.com"));
        AuthorisationCredentials oAuthCredentials = oAuth(accessToken("XystZ5ee"), Url.url("http://baddotrobot.com"));
        HttpClient http = client.with(basicAuthCredentials).with(oAuthCredentials);
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
        client.get(Url.url("http://baddotrobot.com"), headers(UserAgent.SafariOnMac));
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
