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

package bad.robot.http;

import bad.robot.http.configuration.AuthorisationCredentials;
import bad.robot.http.configuration.Proxy;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static bad.robot.http.HeaderList.headers;
import static bad.robot.http.HeaderPair.header;
import static bad.robot.http.HttpClients.anApacheClient;
import static bad.robot.http.configuration.AuthorisationToken.authorisationToken;
import static bad.robot.http.configuration.BasicAuthCredentials.basicAuth;
import static bad.robot.http.configuration.OAuthCredentials.oAuth;
import static bad.robot.http.configuration.Password.password;
import static bad.robot.http.configuration.Username.username;
import static bad.robot.http.matchers.Matchers.has;
import static bad.robot.http.matchers.Matchers.status;
import static org.hamcrest.MatcherAssert.assertThat;

public class HttpClientExamples {

    @Test
    public void exampleGet() throws MalformedURLException {
        HttpResponse response = anApacheClient().get(new URL("http://www.baddotrobot.com"));
        assertThat(response, has(status(200)));
    }

    @Test
    public void exampleGetSettingHeaders() throws MalformedURLException {
        HttpResponse response = anApacheClient().get(new URL("http://www.baddotrobot.com"),
            headers(
                header("Accept", "application/json")
            ));
        assertThat(response, has(status(200)));
    }

    @Test
    public void exampleBasicAuth() throws MalformedURLException {
        AuthorisationCredentials credentials = basicAuth(username("username"), password("secret"), new URL("http://www.baddotrobot.com"));
        HttpResponse response = anApacheClient()
            .with(credentials)
            .get(new URL("http://www.baddotrobot.com"));
        assertThat(response, has(status(200)));
    }

    @Test
    public void exampleOAuthToken() throws MalformedURLException {
        AuthorisationCredentials credentials = oAuth(authorisationToken("XystZ5ee"), new URL("http://www.baddotrobot.com"));
        HttpResponse response = anApacheClient().with(credentials).get(new URL("http://www.baddotrobot.com"));
        assertThat(response, has(status(200)));
    }

    @Test
    public void exampleMixedAuthorisationSchemes() throws MalformedURLException {
        AuthorisationCredentials basicAuthCredentials = basicAuth(username("username"), password("secret"), new URL("http://robotooling.com"));
        AuthorisationCredentials oAuthCredentials = oAuth(authorisationToken("XystZ5ee"), new URL("http://baddotrobot.com"));
        HttpClient http = anApacheClient().with(basicAuthCredentials).with(oAuthCredentials);
        http.get(new URL("http://baddotrobot.com"));
        http.get(new URL("http://robotooling.com"));
    }

    @Test
    public void exampleProxy() throws MalformedURLException {
        anApacheClient().with(Proxy.proxy(new URL("http://localhost:8888"))).get(new URL("http://www.baddotrobot.com"));
    }
}
