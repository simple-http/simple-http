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
import java.util.ArrayList;
import java.util.List;

import static bad.robot.http.configuration.HttpTimeout.httpTimeout;
import static com.google.code.tempusfugit.temporal.Duration.minutes;
import static com.google.code.tempusfugit.temporal.Duration.seconds;
import static org.apache.http.client.params.ClientPNames.*;
import static org.apache.http.params.CoreConnectionPNames.CONNECTION_TIMEOUT;
import static org.apache.http.params.CoreConnectionPNames.SO_TIMEOUT;
import static org.apache.http.params.CoreProtocolPNames.USE_EXPECT_CONTINUE;

public class ApacheHttpClientBuilder implements Builder<org.apache.http.client.HttpClient>, ConfigurableHttpClient {

    private Ssl ssl = Ssl.enabled;
    private List<Credentials> credentials = new ArrayList<Credentials>();
    private Setting<URL> proxy = new NoProxy();
    private Setting<Integer> timeout = httpTimeout(minutes(10));
    private Setting<Boolean> handleRedirects = AutomaticRedirectHandling.on();

    public static ApacheHttpClientBuilder anApacheClientWithShortTimeout() {
        return new ApacheHttpClientBuilder().with(httpTimeout(seconds(5)));
    }

    @Override
    public void withCredentials(String username, String password) {
        this.credentials.add(new UsernamePasswordCredentials(username, password));
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
        HttpParams parameters = createHttpParametersViaNastyHackButBetterThanCopyAndPaste();
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

    protected HttpParams createHttpParametersViaNastyHackButBetterThanCopyAndPaste() {
        return new DefaultHttpClient() {
            @Override
            protected HttpParams createHttpParams() {
                return super.createHttpParams();
            }
        }.createHttpParams();
    }

    private void setupAuthorisation(DefaultHttpClient client) {
        CredentialsProvider credentialsProvider = client.getCredentialsProvider();
        for (Credentials credentials : this.credentials)
            credentialsProvider.setCredentials(AuthScope.ANY, credentials);
    }

}
