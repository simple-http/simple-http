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

import bad.robot.http.Builder;
import bad.robot.http.configuration.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static bad.robot.http.configuration.HttpTimeout.httpTimeout;
import static com.google.code.tempusfugit.temporal.Duration.minutes;
import static com.google.code.tempusfugit.temporal.Duration.seconds;
import static org.apache.http.auth.AuthScope.ANY_REALM;
import static org.apache.http.auth.AuthScope.ANY_SCHEME;
import static org.apache.http.client.params.ClientPNames.*;
import static org.apache.http.params.CoreConnectionPNames.CONNECTION_TIMEOUT;
import static org.apache.http.params.CoreConnectionPNames.SO_TIMEOUT;
import static org.apache.http.params.CoreProtocolPNames.USE_EXPECT_CONTINUE;

public class ApacheHttpClientBuilder implements Builder<org.apache.http.client.HttpClient>, ConfigurableHttpClient {

    private Ssl ssl = Ssl.enabled;
    private Map<URL, Credentials> credentials = new HashMap<URL, Credentials>();
    private Setting<URL> proxy = new NoProxy();
    private Setting<Integer> timeout = httpTimeout(minutes(10));
    private Setting<Boolean> handleRedirects = AutomaticRedirectHandling.on();

    public static ApacheHttpClientBuilder anApacheClientWithShortTimeout() {
        return new ApacheHttpClientBuilder().with(httpTimeout(seconds(5)));
    }

    @Override
    public ApacheHttpClientBuilder withCredentials(String username, String password, URL url) {
        this.credentials.put(url, new UsernamePasswordCredentials(username, password));
        return this;
    }

    public ApacheHttpClientBuilder with(HttpTimeout timeout) {
        this.timeout = timeout;
        return this;
    }

    public ApacheHttpClientBuilder with(Proxy proxy) {
        this.proxy = proxy;
        return this;
    }

    public ApacheHttpClientBuilder with(Ssl ssl) {
        this.ssl = ssl;
        return this;
    }

    public ApacheHttpClientBuilder with(AutomaticRedirectHandling handleRedirects) {
        this.handleRedirects = handleRedirects;
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
        registry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
        registry.register(new Scheme("https", 443, ssl.getSocketFactory()));
        return registry;
    }

    private HttpParams createAndConfigureHttpParameters() {
        HttpParams parameters = createHttpParametersViaNastyHackButBetterThanCopyAndPastingTheApacheSource();
        parameters.setParameter(ALLOW_CIRCULAR_REDIRECTS, true);
        parameters.setParameter(HANDLE_AUTHENTICATION, true);
        parameters.setParameter(USE_EXPECT_CONTINUE, true);

        ApacheHttpParameters apache = new ApacheHttpParameters(parameters);
        handleRedirects.applyTo(apache.configuration(HANDLE_REDIRECTS));
        timeout.applyTo(apache.configuration(CONNECTION_TIMEOUT));
        timeout.applyTo(apache.configuration(SO_TIMEOUT));
        proxy.applyTo(apache.defaultProxy());

        return parameters;
    }

    private HttpParams createHttpParametersViaNastyHackButBetterThanCopyAndPastingTheApacheSource() {
        return new DefaultHttpClient() {
            @Override
            protected HttpParams createHttpParams() {
                return super.createHttpParams();
            }
        }.createHttpParams();
    }

    /* not currently supporting realms and schemes (example schemes are "basic" or "digest") */
    private void setupAuthorisation(DefaultHttpClient client) {
        CredentialsProvider credentialsProvider = client.getCredentialsProvider();
        for (Map.Entry<URL, Credentials> credential : this.credentials.entrySet()) {
            URL url = credential.getKey();
            credentialsProvider.setCredentials(new AuthScope(url.getHost(), url.getPort(), ANY_REALM, ANY_SCHEME), credential.getValue());
        }
    }

}
