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

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.*;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Test;
import simplehttp.*;

import java.io.IOException;
import java.net.URL;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static simplehttp.Any.anyUrl;
import static simplehttp.EmptyHeaders.emptyHeaders;
import static simplehttp.FormParameters.params;
import static simplehttp.HeaderList.headers;
import static simplehttp.HeaderPair.header;
import static simplehttp.matchers.Matchers.*;

public class ApacheHttpClientTest {

    private final JUnitRuleMockery context = new JUnitRuleMockery();
    private final Builder<HttpClient> httpClientBuilder = context.mock(Builder.class, "http client");
    private final Builder<HttpContext> contextBuilder = context.mock(Builder.class, "local context");
    private final HttpClient client = context.mock(HttpClient.class, "apache http");
    private final HttpResponse response = new StringHttpResponse(200, "OK", "", emptyHeaders(), "http://example.com");
    private final HttpContext localContext = new StubHttpContext();

    @Test (expected = HttpException.class)
    public void wrapsExceptionsForGet() {
        context.checking(new Expectations() {{
            oneOf(httpClientBuilder).build(); will(returnValue(client));
            oneOf(contextBuilder).build(); will(returnValue(null));
            oneOf(client); will(throwException(new IOException()));
        }});
        ApacheHttpClient http = new ApacheHttpClient(httpClientBuilder, contextBuilder);
        http.get(anyUrl());
    }

    @Test
    public void executesGet() throws IOException {
        context.checking(new Expectations() {{
            oneOf(httpClientBuilder).build(); will(returnValue(client));
            oneOf(contextBuilder).build(); will(returnValue(localContext));
            oneOf(client).execute(with(Matchers.<HttpGet>instanceOf(HttpGet.class)), with(any(ResponseHandler.class)), with(any(HttpContext.class))); will(returnValue(response));
        }});
        ApacheHttpClient http = new ApacheHttpClient(httpClientBuilder, contextBuilder);
        assertThat(http.get(anyUrl()), is(response));
    }

    @Test
    public void executesGetWithHeaders() throws IOException {
        Headers headers = headers(header("header", "value"));
        expectingHttpClientExecuteWith(requestContaining(headers));
        new ApacheHttpClient(httpClientBuilder, contextBuilder).get(anyUrl(), headers);
    }

    @Test
    public void shouldGotoUrl() throws IOException {
        URL url = anyUrl();
        expectingHttpClientExecuteWith(requestWith(url));
        ApacheHttpClient http = new ApacheHttpClient(httpClientBuilder, contextBuilder);
        http.get(url);
    }

    @Test
    public void shouldUseHttpResponseHandlerToProcessTheResponseWhichCleansUpAfterConsumption() throws IOException {
        URL url = anyUrl();
        context.checking(new Expectations(){{
            oneOf(httpClientBuilder).build(); will(returnValue(client));
            oneOf(contextBuilder).build(); will(returnValue(localContext));
            oneOf(client).execute(with(any(HttpUriRequest.class)), (ResponseHandler) with(Matchers.<HttpResponseHandler>instanceOf(HttpResponseHandler.class)), with(any(HttpContext.class))); will(returnValue(response));
        }});
        ApacheHttpClient http = new ApacheHttpClient(httpClientBuilder, contextBuilder);
        http.get(url);
    }

    @Test
    public void executesPost() throws IOException {
        context.checking(new Expectations() {{
            oneOf(httpClientBuilder).build(); will(returnValue(client));
            oneOf(contextBuilder).build(); will(returnValue(localContext));
            oneOf(client).execute((HttpUriRequest) with(Matchers.<simplehttp.HttpPost>instanceOf(org.apache.http.client.methods.HttpPost.class)), with(any(ResponseHandler.class)), with(any(HttpContext.class))); will(returnValue(response));
        }});

        ApacheHttpClient http = new ApacheHttpClient(httpClientBuilder, contextBuilder);
        simplehttp.HttpPost message = new FormUrlEncodedMessage(params("naughts", "crosses"));
        assertThat(http.post(anyUrl(), message), is(response));
    }

    @Test
    public void executesPostWithHeaders() throws IOException {
        Headers headers = headers(header("header", "value"));
        expectingHttpClientExecuteWith(requestContaining(headers));
        ApacheHttpClient http = new ApacheHttpClient(httpClientBuilder, contextBuilder);
        http.post(anyUrl(), new FormUrlEncodedMessage(params("chalk", "cheese"), headers));
    }

    @Test
    public void executesPostWithStringMessage() throws IOException {
        expectingHttpClientExecuteWith(messageContaining("these aren't the droids you're looking for..."));
        ApacheHttpClient http = new ApacheHttpClient(httpClientBuilder, contextBuilder);
        http.post(anyUrl(), new UnencodedStringMessage("these aren't the droids you're looking for..."));
    }

    @Test
    public void executesPostWithUrlFormEncodedMessage() throws IOException {
        expectingHttpClientExecuteWith(messageContaining("name=value"));
        ApacheHttpClient http = new ApacheHttpClient(httpClientBuilder, contextBuilder);
        http.post(anyUrl(), new FormUrlEncodedMessage(params("name", "value")));
    }

    @Test
    public void executesPut() throws IOException {
        context.checking(new Expectations() {{
            oneOf(httpClientBuilder).build(); will(returnValue(client));
            oneOf(contextBuilder).build(); will(returnValue(localContext));
            oneOf(client).execute(with(Matchers.<HttpPut>instanceOf(HttpPut.class)), with(any(ResponseHandler.class)), with(any(HttpContext.class))); will(returnValue(response));
        }});

        ApacheHttpClient http = new ApacheHttpClient(httpClientBuilder, contextBuilder);
        HttpResponse actualResponse = http.put(anyUrl(), new UnencodedStringMessage(""));
        assertThat(actualResponse, is(response));
    }

    @Test
    public void executesPutWithHeaders() throws IOException {
        Headers headers = headers(header("header", "value"));
        expectingHttpClientExecuteWith(requestContaining(headers));
        ApacheHttpClient http = new ApacheHttpClient(httpClientBuilder, contextBuilder);
        http.put(anyUrl(), new UnencodedStringMessage("", headers));
    }

    @Test
    public void executesPutWithStringMessage() throws IOException {
        expectingHttpClientExecuteWith(messageContaining("that's no moon!"));
        ApacheHttpClient http = new ApacheHttpClient(httpClientBuilder, contextBuilder);
        http.put(anyUrl(), new UnencodedStringMessage("that's no moon!"));
    }

    @Test
    public void executesDelete() throws IOException {
        context.checking(new Expectations() {{
            oneOf(httpClientBuilder).build(); will(returnValue(client));
            oneOf(contextBuilder).build(); will(returnValue(localContext));
            oneOf(client).execute(with(Matchers.<HttpDelete>instanceOf(HttpDelete.class)), with(any(ResponseHandler.class)), with(any(HttpContext.class))); will(returnValue(response));
        }});

        ApacheHttpClient http = new ApacheHttpClient(httpClientBuilder, contextBuilder);
        assertThat(http.delete(anyUrl()), is(response));
    }

    @Test
    public void executesOptions() throws IOException {
        context.checking(new Expectations() {{
            oneOf(httpClientBuilder).build(); will(returnValue(client));
            oneOf(contextBuilder).build(); will(returnValue(localContext));
            oneOf(client).execute(with(Matchers.<HttpOptions>instanceOf(HttpOptions.class)), with(any(ResponseHandler.class)), with(any(HttpContext.class))); will(returnValue(response));
        }});

        ApacheHttpClient http = new ApacheHttpClient(httpClientBuilder, contextBuilder);
        assertThat(http.options(anyUrl()), is(response));
    }

    @Test
    public void shouldCloseConnectionManager() {
        final ClientConnectionManager connectionManager = context.mock(ClientConnectionManager.class);
        context.checking(new Expectations(){{
            allowing(httpClientBuilder).build(); will(returnValue(client));
            allowing(contextBuilder).build(); will(returnValue(null));
            allowing(client).getConnectionManager(); will(returnValue(connectionManager));
            oneOf(connectionManager).shutdown();
        }});
        new ApacheHttpClient(httpClientBuilder, contextBuilder).shutdown();
    }

    private void expectingHttpClientExecuteWith(Matcher<? extends HttpUriRequest> request) throws IOException {
        expectingHttpClientExecuteWith(request, any(ResponseHandler.class));
    }

    private void expectingHttpClientExecuteWith(final Matcher<? extends HttpUriRequest> request, final Matcher<ResponseHandler> responseHandler) throws IOException {
        context.checking(new Expectations() {{
            oneOf(httpClientBuilder).build(); will(returnValue(client));
            oneOf(contextBuilder).build(); will(returnValue(localContext));
            oneOf(client).execute(with(request), with(responseHandler), with(any(HttpContext.class))); will(returnValue(response));
        }});
    }

    private static class StubHttpContext implements HttpContext {
        @Override
        public Object getAttribute(String id) {
            return null;
        }

        @Override
        public void setAttribute(String id, Object obj) {
        }

        @Override
        public Object removeAttribute(String id) {
            return null;
        }
    }
}
