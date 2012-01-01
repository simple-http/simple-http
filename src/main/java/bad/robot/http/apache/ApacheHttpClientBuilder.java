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

package bad.robot.http.apache;

import bad.robot.http.Builder;
import com.google.code.tempusfugit.temporal.Duration;
import org.apache.http.HttpHost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;

import java.util.ArrayList;
import java.util.List;

import static com.google.code.tempusfugit.temporal.Duration.minutes;
import static com.google.code.tempusfugit.temporal.Duration.seconds;
import static org.apache.http.client.params.ClientPNames.*;
import static org.apache.http.conn.params.ConnRoutePNames.DEFAULT_PROXY;
import static org.apache.http.params.CoreConnectionPNames.CONNECTION_TIMEOUT;
import static org.apache.http.params.CoreConnectionPNames.SO_TIMEOUT;
import static org.apache.http.params.CoreProtocolPNames.USE_EXPECT_CONTINUE;

public class ApacheHttpClientBuilder implements Builder<org.apache.http.client.HttpClient> {

    private Duration timeout = minutes(10);
    private List<ApacheHttpAuthenticationCredentials> credentials = new ArrayList<ApacheHttpAuthenticationCredentials>();
    private HttpHost proxy;

    public static ApacheHttpClientBuilder anApacheClientWithShortTimeout() {
        return new ApacheHttpClientBuilder().with(seconds(5));
    }

    public ApacheHttpClientBuilder with(Duration timeout) {
        this.timeout = timeout;
        return this;
    }

    public ApacheHttpClientBuilder withProxy(HttpHost proxy) {
        this.proxy = proxy;
        return this;
    }

    public ApacheHttpClientBuilder with(ApacheHttpAuthenticationCredentials login) {
        credentials.add(login);
        return this;
    }

    public org.apache.http.client.HttpClient build() {
        HttpParams httpParameters = createAndConfigureHttpParameters();
        ThreadSafeClientConnManager connectionManager = new ThreadSafeClientConnManager(httpParameters, createSchemeRegistry());
        DefaultHttpClient client = new DefaultHttpClient(connectionManager, httpParameters);
        setupAuthorisation(client);
        return client;
    }

    private SchemeRegistry createSchemeRegistry() {
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        return registry;
    }

    private HttpParams createAndConfigureHttpParameters() {
        HttpParams parameters = createHttpParametersViaNastyHackButBetterThanCopyAndPaste();
        parameters.setParameter(CONNECTION_TIMEOUT, (int) timeout.inMillis());
        parameters.setParameter(SO_TIMEOUT, (int) timeout.inMillis());
        parameters.setParameter(HANDLE_REDIRECTS, true);
        parameters.setParameter(ALLOW_CIRCULAR_REDIRECTS, true);
        parameters.setParameter(HANDLE_AUTHENTICATION, true);
        parameters.setParameter(USE_EXPECT_CONTINUE, true);
        parameters.setParameter(DEFAULT_PROXY, proxy);
        HttpClientParams.setRedirecting(parameters, true);
        return parameters;
    }

    protected HttpParams createHttpParametersViaNastyHackButBetterThanCopyAndPaste() {
        return new DefaultHttpClient() {
            @Override
            protected HttpParams createHttpParams() {
                return super.createHttpParams();
            }
        }.createHttpParams();
    }

    private void setupAuthorisation(DefaultHttpClient client) {
        for (ApacheHttpAuthenticationCredentials credentials : this.credentials)
            client.getCredentialsProvider().setCredentials(credentials.getScope(), credentials.getUser());
    }

}
