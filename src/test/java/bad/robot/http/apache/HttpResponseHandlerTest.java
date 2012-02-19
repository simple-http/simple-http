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

import bad.robot.http.HttpResponse;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static bad.robot.http.SimpleHeader.header;
import static bad.robot.http.matchers.Matchers.*;
import static org.apache.http.HttpVersion.HTTP_1_1;
import static org.junit.Assert.assertThat;

@RunWith(JMock.class)
public class HttpResponseHandlerTest {

    private final Mockery context = new Mockery();

    private final HttpEntity body = context.mock(HttpEntity.class);

    private final BasicHttpResponse anApacheOkResponse = new BasicHttpResponse(new BasicStatusLine(HTTP_1_1, 200, "OK"));
    private final BasicHttpResponse anApacheResponseWithHeaders = new BasicHttpResponse(new BasicStatusLine(HTTP_1_1, 200, "OK")) {{
        setHeaders(new Header[]{ new BasicHeader("Accept", "text/html"), new BasicHeader("Content-Type", "application/json")});
    }};
    private final BasicHttpResponse anApacheResponseWithBody = new BasicHttpResponse(new BasicStatusLine(HTTP_1_1, 400, "Bad Request")) {{
        setEntity(body);
    }};

    private final ContentConsumingStrategy consumer = context.mock(ContentConsumingStrategy.class);
    private final HttpResponseHandler handler = new HttpResponseHandler(consumer);

    @Test
    public void shouldConvertStatusCode() throws IOException {
        HttpResponse response = handler.handleResponse(anApacheOkResponse);
        assertThat(response, hasStatus(200));
    }

    @Test
    public void shouldConvertStatusMessage() throws IOException {
        HttpResponse response = handler.handleResponse(anApacheOkResponse);
        assertThat(response, hasStatusMessage("OK"));
    }

    @Test
    public void shouldConvertEmptyBody() throws IOException {
        HttpResponse response = handler.handleResponse(anApacheOkResponse);
        assertThat(response, hasContent(""));
    }

    @Test
    public void shouldConvertBody() throws IOException {
        context.checking(new Expectations(){{
            one(consumer).toString(body); will(returnValue("I'm a http message body"));
        }});
        HttpResponse response = handler.handleResponse(anApacheResponseWithBody);
        assertThat(response, hasContent("I'm a http message body"));
    }

    @Test
    public void shouldConvertHeaders() throws IOException {
        HttpResponse response = handler.handleResponse(anApacheResponseWithHeaders);
        assertThat(response, hasHeader(header("Accept", "text/html")));
        assertThat(response, hasHeader(header("Content-Type", "application/json")));
    }

    @Test
    public void shouldCleanupContent() throws IOException {
        context.checking(new Expectations(){{
            ignoring(consumer);
        }});
        handler.handleResponse(anApacheResponseWithBody);
    }

    @Test (expected = IOException.class)
    public void shouldCleanupContentEvenOnException() throws IOException {
        context.checking(new Expectations(){{
            allowing(consumer); will(throwException(new IOException()));
        }});
        handler.handleResponse(anApacheResponseWithBody);
    }

}
