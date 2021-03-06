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

import simplehttp.configuration.AuthorisationCredentials;
import simplehttp.configuration.AutomaticRedirectHandling;
import simplehttp.configuration.HttpTimeout;
import simplehttp.configuration.Proxy;

/**
 * The common configuration interface for all {@link HttpClient} implementations.
 *
 * Clients may or may not implement the various configuration parameters and they may or may not take affect after actual
 * {@link HttpClient} calls. For example, you might set a timeout using the {@link CommonHttpClient#with(HttpTimeout)}
 * and then call the {@link HttpClient#get(java.net.URL)} to perform a HTTP GET. The underlying HTTP client implementation
 * should respect the timeout configuration but is free to ignore subsequent calls to set the timeout (ie, implementations
 * may choose to lazy load the underlying client).
 *
 */
public interface CommonHttpClient extends HttpClient {

    CommonHttpClient with(AuthorisationCredentials credentials);

    CommonHttpClient withoutSsl();

    CommonHttpClient withTrustingSsl();

    CommonHttpClient with(AutomaticRedirectHandling handleRedirects);

    CommonHttpClient with(Proxy proxy);

    CommonHttpClient with(HttpTimeout timeout);
}
