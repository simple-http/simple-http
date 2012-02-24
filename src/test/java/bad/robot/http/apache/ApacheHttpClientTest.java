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
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static bad.robot.http.FormParameters.params;
import static bad.robot.http.SimpleHeader.header;
import static bad.robot.http.SimpleHeaders.headers;
import static bad.robot.http.SimpleHeaders.noHeaders;
import static bad.robot.http.matchers.Matchers.*;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsAnything.any;
import static org.junit.Assert.assertThat;

@RunWith(JMock.class)
public class ApacheHttpClientTest {

    private final Mockery context = new Mockery();
    private final Builder<HttpClient> builder = context.mock(Builder.class);
    private final HttpClient client = context.mock(HttpClient.class, "apache http");
    private final HttpResponse response = new DefaultHttpResponse(200, "OK", "", noHeaders());

    @Test
    public void executesGet() throws IOException {
        context.checking(new Expectations() {{
            one(builder).build(); will(returnValue(client));
            one(client).execute((HttpUriRequest) with(instanceOf(HttpGet.class)), with(any(ResponseHandler.class))); will(returnValue(response));
        }});
        ApacheHttpClient http = new ApacheHttpClient(builder);
        assertThat(http.get(anyUrl()), is(response));
    }

    @Test
    public void executesGetWithHeaders() throws IOException {
        Headers headers = headers(header("header", "value"));
        expectingHttpClientExecuteWith(requestContaining(headers));
        new ApacheHttpClient(builder).get(anyUrl(), headers);        
    }

    @Test (expected = HttpException.class)
    public void wrapExceptionsForGet() throws MalformedURLException {
        context.checking(new Expectations() {{
            one(builder).build(); will(returnValue(client));
            one(client); will(throwException(new IOException()));
        }});
        ApacheHttpClient http = new ApacheHttpClient(builder);
        http.get(anyUrl());
    }

    @Test
    public void shouldGotoUrl() throws IOException {
        URL url = anyUrl();
        expectingHttpClientExecuteWith(requestWith(url));
        ApacheHttpClient http = new ApacheHttpClient(builder);
        http.get(url);
    }

    @Test
    public void shouldUseHttpResponseHandlerToProcessTheResponse_WhichCleansUpAfterConsumption() throws IOException {
        URL url = anyUrl();
        context.checking(new Expectations(){{
            one(builder).build(); will(returnValue(client));
            one(client).execute(with(any(HttpUriRequest.class)), (ResponseHandler) with(instanceOf(HttpResponseHandler.class))); will(returnValue(response));
        }});
        ApacheHttpClient http = new ApacheHttpClient(builder);
        http.get(url);
    }

    @Test
    public void executesPost() throws IOException {
        context.checking(new Expectations() {{
            one(builder).build(); will(returnValue(client));
            one(client).execute((HttpUriRequest) with(instanceOf(HttpPost.class)), with(any(ResponseHandler.class))); will(returnValue(response));
        }});

        ApacheHttpClient http = new ApacheHttpClient(builder);
        HttpPostMessage message = new FormUrlEncodedMessage(params("naughts", "crosses"));
        assertThat(http.post(anyUrl(), message), is(response));
    }

    @Test
    public void executesPostWithHeaders() throws IOException {
        Headers headers = headers(header("header", "value"));
        expectingHttpClientExecuteWith(requestContaining(headers));
        ApacheHttpClient http = new ApacheHttpClient(builder);
        http.post(anyUrl(), new FormUrlEncodedMessage(params("chalk", "cheese"), headers));
    }

    @Test
    public void executesPostWithStringMessage() throws IOException {
        expectingHttpClientExecuteWith(messageContaining("these aren't the droids you're looking for..."));
        ApacheHttpClient http = new ApacheHttpClient(builder);
        http.post(anyUrl(), new UnencodedStringMessage("these aren't the droids you're looking for..."));
    }

    @Test
    public void executesPostWithUrlFormEncodedMessage() throws IOException {
        expectingHttpClientExecuteWith(messageContaining("name=value"));
        ApacheHttpClient http = new ApacheHttpClient(builder);
        http.post(anyUrl(), new FormUrlEncodedMessage(params("name", "value")));
    }

    @Test
    public void executesPut() throws IOException {
        context.checking(new Expectations() {{
            one(builder).build(); will(returnValue(client));
            one(client).execute((HttpUriRequest) with(instanceOf(HttpPut.class)), with(any(ResponseHandler.class))); will(returnValue(response));
        }});

        ApacheHttpClient http = new ApacheHttpClient(builder);
        HttpResponse actualResponse = http.put(anyUrl(), new UnencodedStringMessage(""));
        assertThat(actualResponse, is(response));
    }

    @Test
    public void executesPutWithHeaders() throws IOException {
        Headers headers = headers(header("header", "value"));
        expectingHttpClientExecuteWith(requestContaining(headers));
        ApacheHttpClient http = new ApacheHttpClient(builder);
        http.put(anyUrl(), new UnencodedStringMessage("", headers));
    }

    @Test
    public void executesPutWithStringMessage() throws IOException {
        expectingHttpClientExecuteWith(messageContaining("that's no moon!"));
        ApacheHttpClient http = new ApacheHttpClient(builder);
        http.put(anyUrl(), new UnencodedStringMessage("that's no moon!"));
    }

    @Test
    public void shouldCloseConnectionManager() throws IOException {
        final ClientConnectionManager connectionManager = context.mock(ClientConnectionManager.class);
        context.checking(new Expectations(){{
            allowing(builder).build(); will(returnValue(client));
            allowing(client).getConnectionManager(); will(returnValue(connectionManager));
            one(connectionManager).shutdown();
        }});
        new ApacheHttpClient(builder).shutdown();
    }


    private void expectingHttpClientExecuteWith(Matcher<? extends HttpUriRequest> request) throws IOException {
        expectingHttpClientExecuteWith(request, any(ResponseHandler.class));
    }

    private void expectingHttpClientExecuteWith(final Matcher<? extends HttpUriRequest> request, final Matcher<ResponseHandler> responseHandler) throws IOException {
        context.checking(new Expectations() {{
            one(builder).build(); will(returnValue(client));
            one(client).execute(with(request), with(responseHandler)); will(returnValue(response));
        }});
    }

    private static URL anyUrl() throws MalformedURLException {
        return new URL("http://not.real.url");
    }

}
