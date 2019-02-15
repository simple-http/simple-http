/*
 * Copyright (c) 2011-2018, simple-http committers
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

import simplehttp.apache.ApacheAuthenticationSchemeHttpContextBuilder;
import simplehttp.apache.ApacheHttpClient;
import simplehttp.apache.ApacheHttpClientBuilder;
import simplehttp.apache.Ssl;
import simplehttp.configuration.AuthorisationCredentials;
import simplehttp.configuration.AutomaticRedirectHandling;
import simplehttp.configuration.HttpTimeout;
import simplehttp.configuration.Proxy;

import java.net.URL;

import static simplehttp.apache.ApacheAuthenticationSchemeHttpContextBuilder.anApacheBasicAuthScheme;
import static simplehttp.apache.ApacheHttpClientBuilder.anApacheClientWithShortTimeout;

public class HttpClients {

    private HttpClients() { }

    public static CommonHttpClient anApacheClient() {
        return new ApacheCommonHttpClient();
    }

    private static class ApacheCommonHttpClient implements CommonHttpClient {

        private final ApacheHttpClientBuilder apache = anApacheClientWithShortTimeout();
        private final ApacheAuthenticationSchemeHttpContextBuilder authenticationSchemes = anApacheBasicAuthScheme();

        private ApacheHttpClient httpClient;

        @Override
        public CommonHttpClient with(AuthorisationCredentials credentials) {
            credentials.applyTo(apache);
            credentials.applyTo(authenticationSchemes);
            return this;
        }

        @Override
        public CommonHttpClient with(HttpTimeout timeout) {
            apache.with(timeout);
            return this;
        }

        @Override
        public CommonHttpClient withoutSsl() {
            apache.with(Ssl.disabled);
            return this;
        }

        @Override
        public CommonHttpClient withTrustingSsl() {
            apache.with(Ssl.naive);
            return this;
        }

        @Override
        public CommonHttpClient with(AutomaticRedirectHandling handleRedirects) {
            apache.with(handleRedirects);
            return this;
        }

        @Override
        public CommonHttpClient with(Proxy proxy) {
            apache.with(proxy);
            return this;
        }

        @Override
        public HttpResponse get(URL url) throws HttpException {
            initialiseHttpClient();
            return httpClient.get(url);
        }

        @Override
        public HttpResponse get(URL url, Headers headers) throws HttpException {
            initialiseHttpClient();
            return httpClient.get(url, headers);
        }

        @Override
        public HttpResponse post(URL url, HttpPost message) throws HttpException {
            initialiseHttpClient();
            return httpClient.post(url, message);
        }

        @Override
        public HttpResponse put(URL url, HttpPut message) throws HttpException {
            initialiseHttpClient();
            return httpClient.put(url, message);
        }

        @Override
        public HttpResponse delete(URL url) throws HttpException {
            initialiseHttpClient();
            return httpClient.delete(url);
        }

        @Override
        public HttpResponse options(URL url) throws HttpException {
            initialiseHttpClient();
            return httpClient.options(url);
        }

        @Override
        public void shutdown() {
            initialiseHttpClient();
            httpClient.shutdown();
        }

        private void initialiseHttpClient() {
            if (httpClient == null)
                httpClient = new ApacheHttpClient(apache, authenticationSchemes);
        }
    }

}
