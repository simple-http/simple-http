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
import org.apache.http.HttpHost;
import org.apache.http.client.AuthCache;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import static org.apache.http.client.protocol.ClientContext.AUTH_CACHE;

public class ApacheHttpContextBuilder implements Builder<HttpContext> {

    private final BasicHttpContext localContext = new BasicHttpContext();
    private final AuthCache authenticationCache = new BasicAuthCache();

    private ApacheHttpContextBuilder() {
    }

    public static ApacheHttpContextBuilder anApacheHttpContextBuilder() {
        return new ApacheHttpContextBuilder();
    }

    public ApacheHttpContextBuilder withBasicAuth(HttpHost host) {
        authenticationCache.put(new HttpHost(host.getHostName(), host.getPort(), host.getSchemeName()), new BasicScheme());
        return this;
    }

    @Override
    public HttpContext build() {
        localContext.setAttribute(AUTH_CACHE, authenticationCache);
        return localContext;
    }
}
