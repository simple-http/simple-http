/*
 * Copyright (c) 2009-2012, bad robot (london) ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package bad.robot.http;

import bad.robot.http.apache.ApacheHttpAuthenticationCredentials;
import bad.robot.http.apache.ApacheHttpClient;
import com.google.code.tempusfugit.temporal.Duration;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;

import java.net.URL;

import static bad.robot.http.apache.ApacheHttpClientBuilder.anApacheClientWithShortTimeout;

public class HttpClients {

    public static CommonHttpClientBuilder anApacheClient() {
        return new ApacheHttpClientBuilder();
    }

    private static class ApacheHttpClientBuilder implements CommonHttpClientBuilder {

        private final bad.robot.http.apache.ApacheHttpClientBuilder apacheBuilder = anApacheClientWithShortTimeout();

        private ApacheHttpClient httpClient;

        @Override
        public CommonHttpClientBuilder with(String username, String password) {
            apacheBuilder.with(new ApacheHttpAuthenticationCredentials(new AuthScope(AuthScope.ANY), new UsernamePasswordCredentials(username, password)));
            return this;
        }

        @Override
        public CommonHttpClientBuilder with(Duration timeout) {
            apacheBuilder.with(timeout);
            return this;
        }

        @Override
        public HttpResponse get(URL url) throws HttpException {
            if (httpClient == null)
                httpClient = new ApacheHttpClient(apacheBuilder);
            return httpClient.get(url);
        }

        @Override
        public HttpResponse get(URL url, Headers headers) throws HttpException {
            if (httpClient == null)
                httpClient = new ApacheHttpClient(apacheBuilder);
            return httpClient.get(url, headers);
        }

        @Override
        public void shutdown() {
            if (httpClient == null)
                httpClient = new ApacheHttpClient(apacheBuilder);
            httpClient.shutdown();
        }
    }
}
