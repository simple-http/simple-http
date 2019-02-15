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

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.*;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.protocol.HttpContext;
import simplehttp.*;

import java.net.URL;
import java.util.concurrent.Callable;

import static simplehttp.EmptyHeaders.emptyHeaders;
import static simplehttp.apache.Coercions.asApacheBasicHeader;

public class ApacheHttpClient implements HttpClient {

    private final org.apache.http.client.HttpClient client;
    private final HttpContext localContext;
    private final Executor<HttpException> executor;

    public ApacheHttpClient(Builder<org.apache.http.client.HttpClient> clientBuilder, Builder<HttpContext> localContextBuilder) {
        this.client = clientBuilder.build();
        this.localContext = localContextBuilder.build();
        this.executor = new ApacheExceptionWrappingExecutor();
    }

    @Override
    public HttpResponse get(URL url, Headers headers) throws HttpException {
        HttpGet get = new HttpGet(url.toExternalForm());
        get.setHeaders(asApacheBasicHeader(headers));
        return execute(get);
    }

    @Override
    public HttpResponse get(URL url) throws HttpException {
        return get(url, emptyHeaders());
    }

    @Override
    public HttpResponse post(URL url, simplehttp.HttpPost message) throws HttpException {
        HttpPost post = new HttpPost(url.toExternalForm());
        for (Header header : message.getHeaders())
            post.addHeader(header.name(), header.value());
        post.setEntity(new HttpRequestToEntity(message).asHttpEntity());
        return execute(post);
    }

    @Override
    public HttpResponse put(URL url, simplehttp.HttpPut message) {
        HttpPut put = new HttpPut(url.toExternalForm());
        for (Header header : message.getHeaders())
            put.addHeader(header.name(), header.value());
        put.setEntity(new HttpRequestToEntity(message).asHttpEntity());
        return execute(put);
    }

    @Override
    public HttpResponse delete(URL url) throws HttpException {
        return execute(new HttpDelete(url.toExternalForm()));
    }

    @Override
    public HttpResponse options(URL url) throws HttpException {
        return execute(new HttpOptions(url.toExternalForm()));
    }

    private HttpResponse execute(HttpUriRequest request) {
        return executor.submit(http(request));
    }

    private Callable<HttpResponse> http(HttpUriRequest request) {
        return new ApacheHttpRequestExecutor(client, localContext, request);
    }

    @Override
    public void shutdown() {
        client.getConnectionManager().shutdown();
    }

}
