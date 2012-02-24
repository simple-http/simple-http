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
import org.apache.http.client.methods.*;

import java.net.URL;
import java.util.concurrent.Callable;

import static bad.robot.http.SimpleHeaders.noHeaders;
import static bad.robot.http.apache.Coercions.asApacheBasicHeader;
import static com.google.code.tempusfugit.ExceptionWrapper.wrapAnyException;
import static com.google.code.tempusfugit.WithException.with;

public class ApacheHttpClient implements HttpClient {

    private final org.apache.http.client.HttpClient client;

    public ApacheHttpClient(Builder<org.apache.http.client.HttpClient> builder) {
        client = builder.build();
    }

    @Override
    public HttpResponse get(URL url, Headers headers) throws HttpException {
        HttpGet get = new HttpGet(url.toExternalForm());
        get.setHeaders(asApacheBasicHeader(headers));
        return execute(get);
    }

    @Override
    public HttpResponse get(URL url) throws HttpException {
        return get(url, noHeaders());
    }

    @Override
    public HttpResponse post(URL url, HttpPostMessage message) throws HttpException {
        HttpPost post = new HttpPost(url.toExternalForm());
        for (Header header : message.getHeaders())
            post.addHeader(header.name(), header.value());
        post.setEntity(new HttpRequestToEntity(message).asHttpEntity());
        return execute(post);
    }

    @Override
    public HttpResponse put(URL url, HttpPutMessage message) {
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

    private HttpResponse execute(final HttpUriRequest request) {
        return wrapAnyException(new Callable<HttpResponse>() {
            @Override
            public HttpResponse call() throws Exception {
                return client.execute(request, new HttpResponseHandler(new ToStringConsumer()));
            }
        }, with(HttpException.class));
    }

    @Override
    public void shutdown() {
        client.getConnectionManager().shutdown();
    }

}
