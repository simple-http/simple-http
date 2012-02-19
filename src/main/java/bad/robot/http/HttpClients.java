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

import bad.robot.http.apache.ApacheHttpAuthenticationCredentials;
import bad.robot.http.apache.ApacheHttpClient;
import bad.robot.http.apache.Ssl;
import com.google.code.tempusfugit.temporal.Duration;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;

import java.net.URL;

import static bad.robot.http.apache.ApacheHttpClientBuilder.anApacheClientWithShortTimeout;

public class HttpClients {

    public static CommonHttpClient anApacheClient() {
        return new ApacheCommonHttpClient();
    }

    private static class ApacheCommonHttpClient implements CommonHttpClient {

        private final bad.robot.http.apache.ApacheHttpClientBuilder apacheBuilder = anApacheClientWithShortTimeout();

        private ApacheHttpClient httpClient;

        @Override
        public CommonHttpClient with(String username, String password) {
            apacheBuilder.with(new ApacheHttpAuthenticationCredentials(new AuthScope(AuthScope.ANY), new UsernamePasswordCredentials(username, password)));
            return this;
        }

        @Override
        public CommonHttpClient with(Duration timeout) {
            apacheBuilder.with(timeout);
            return this;
        }

        @Override
        public CommonHttpClient withoutSsl() {
            apacheBuilder.with(Ssl.disabled);
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
        public HttpResponse post(URL url, HttpPostMessage message) throws HttpException {
            initialiseHttpClient();
            return httpClient.post(url, message);
        }

        @Override
        public void shutdown() {
            initialiseHttpClient();
            httpClient.shutdown();
        }

        private void initialiseHttpClient() {
            if (httpClient == null)
                httpClient = new ApacheHttpClient(apacheBuilder);
        }
    }
}
