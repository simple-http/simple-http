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

package simplehttp.apache;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.junit.Test;
import simplehttp.configuration.AutomaticRedirectHandling;

import java.time.Duration;

import static java.time.temporal.ChronoUnit.MILLIS;
import static java.time.temporal.ChronoUnit.MINUTES;
import static org.hamcrest.MatcherAssert.assertThat;
import static simplehttp.Url.url;
import static simplehttp.apache.matchers.InternalHttpClientConfigMatcher.hasConfiguration;
import static simplehttp.apache.matchers.InternalHttpClientCredentialsMatcher.credentialsProviderContains;
import static simplehttp.configuration.HttpTimeout.httpTimeout;
import static simplehttp.configuration.Proxy.proxy;

public class ApacheHttpClientBuilderTest {

    private final ApacheHttpClientBuilder builder = new ApacheHttpClientBuilder();
    private final int TEN_MINUTES = (int) Duration.of(10, MINUTES).toMillis();
    
    private final RequestConfig.Builder defaultExpectedConfiguration = RequestConfig.custom()
        .setCircularRedirectsAllowed(true)
        .setAuthenticationEnabled(true)
        .setExpectContinueEnabled(true)
        .setRedirectsEnabled(true)
        .setConnectTimeout(TEN_MINUTES)
        .setSocketTimeout(TEN_MINUTES)
        .setConnectionRequestTimeout(TEN_MINUTES)
        .setProxy(null);

    @Test
    public void shouldDefaultConfigurationValues() {
        HttpClient client = builder.build();
        assertThat(client, hasConfiguration(defaultExpectedConfiguration.build()));
    }

    @Test
    public void shouldDefaultConfigurationValueForPredefinedBuilder() {
        HttpClient client = ApacheHttpClientBuilder.anApacheClientWithShortTimeout().build();
        assertThat(client, hasConfiguration(defaultExpectedConfiguration
            .setConnectTimeout(5000)
            .setSocketTimeout(5000)
            .setConnectionRequestTimeout(5000)
            .build()));
    }

    @Test
    public void shouldConfigureAutomaticRedirectHandling() {
        assertThat(builder.with(AutomaticRedirectHandling.off()).build(), hasConfiguration(defaultExpectedConfiguration.setRedirectsEnabled(false).build()));
        assertThat(builder.with(AutomaticRedirectHandling.on()).build(), hasConfiguration(defaultExpectedConfiguration.setRedirectsEnabled(true).build()));
    }

    @Test
    public void shouldConfigureTimeouts() {
        HttpClient client = builder.with(httpTimeout(Duration.of(256, MILLIS))).build();
        assertThat(client, hasConfiguration(defaultExpectedConfiguration
            .setConnectTimeout(256)
            .setSocketTimeout(256)
            .setConnectionRequestTimeout(256)
            .build()));
    }

    @Test
    public void shouldConfigureProxy() {
        RequestConfig expected = defaultExpectedConfiguration.setProxy(new HttpHost("localhost", 8989, "http")).build();
        assertThat(builder.with(proxy(url("http://localhost:8989"))).build(), hasConfiguration(expected));
    }

    @Test
    public void shouldConfigureCredentialsProvider() {
        HttpClient client = builder.withBasicAuthCredentials("username", "password", url("http://localhost:80")).build();
        assertThat(client, credentialsProviderContains(url("http://localhost:80"), "username", "password"));
    }

    @Test
    public void credentialsProviderCanOnlyHaveOneCredentialPerAuthenticationScope() {
        HttpClient client = builder
            .withBasicAuthCredentials("username", "password", url("http://localhost:80"))
            .withBasicAuthCredentials("replacedUsername", "replacedPassword", url("http://localhost:80"))
            .build();
        assertThat(client, credentialsProviderContains(url("http://localhost:80"), "replacedUsername", "replacedPassword"));
    }

    @Test
    public void credentialsProviderWithDifferingAuthenticationScopes() {
        HttpClient client = builder
            .withBasicAuthCredentials("username", "password", url("http://localhost:80"))
            .withBasicAuthCredentials("replacedUsername", "replacedPassword", url("http://localhost:8081"))
            .build();
        assertThat(client, credentialsProviderContains(url("http://localhost:80"), "username", "password"));
        assertThat(client, credentialsProviderContains(url("http://localhost:8081"), "replacedUsername", "replacedPassword"));
    }

    @Test
    public void credentialsProviderWithDifferingAuthenticationScopesUsingRealUrls() {
        HttpClient client = builder
            .withBasicAuthCredentials("username", "password", url("http://baddotrobot.com"))
            .withBasicAuthCredentials("replacedUsername", "replacedPassword", url("http://robotooling.com"))
            .build();
        assertThat(client, credentialsProviderContains(url("http://baddotrobot.com"), "username", "password"));
        assertThat(client, credentialsProviderContains(url("http://robotooling.com"), "replacedUsername", "replacedPassword"));
    }

    @Test
    public void hashCodeAndEquals() {
        EqualsVerifier.forClass(ApacheHttpClientBuilder.AuthenticatedHost.class).verify();
    }
}
