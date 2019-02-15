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

package simplehttp.configuration;

import java.net.URL;

public class OAuthCredentials implements AuthorisationCredentials {

    private final AccessToken token;
    private final URL url;

    public static OAuthCredentials oAuth(AccessToken token, URL url) {
        return new OAuthCredentials(token, url);
    }

    private OAuthCredentials(AccessToken token, URL url) {
        this.token = token;
        this.url = url;
    }

    @Override
    public void applyTo(ConfigurableHttpClient client) {
        client.withOAuthCredentials(token.value, url);
    }
}
