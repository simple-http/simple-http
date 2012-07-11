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

import bad.robot.http.*;

import java.net.URL;

public class ApacheExceptionWrappingHttpClient implements HttpClient {

    private final HttpClient delegate;

    public ApacheExceptionWrappingHttpClient(HttpClient delegate) {
        this.delegate = delegate;
    }

    @Override
    public HttpResponse get(URL url) throws HttpException {
        return delegate.get(url);
    }

    @Override
    public HttpResponse get(URL url, Headers headers) throws HttpException {
        return delegate.get(url, headers);
    }

    @Override
    public HttpResponse post(URL url, HttpPost message) throws HttpException {
        return delegate.post(url, message);
    }

    @Override
    public HttpResponse put(URL url, HttpPut message) throws HttpException {
        return delegate.put(url, message);
    }

    @Override
    public HttpResponse delete(URL url) throws HttpException {
        return delegate.delete(url);
    }

    @Override
    public HttpResponse options(URL url) throws HttpException {
        return delegate.options(url);
    }

    @Override
    public void shutdown() {
        delegate.shutdown();
    }
}
