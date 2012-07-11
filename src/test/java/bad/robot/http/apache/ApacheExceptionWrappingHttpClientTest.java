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

import bad.robot.http.HttpClient;
import bad.robot.http.UnencodedStringMessage;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URL;

import static bad.robot.http.Any.anyUrl;
import static bad.robot.http.SimpleHeader.header;
import static bad.robot.http.SimpleHeaders.headers;

@RunWith(JMock.class)
public class ApacheExceptionWrappingHttpClientTest {

    private final Mockery context = new JUnit4Mockery();

    private final HttpClient delegate = context.mock(HttpClient.class);
    private final HttpClient client = new ApacheExceptionWrappingHttpClient(delegate);
    private final URL url = anyUrl();
    private final UnencodedStringMessage content = new UnencodedStringMessage("content");

    @Test
    public void delegatesDelete() {
        context.checking(new Expectations() {{
            oneOf(delegate).delete(url);
        }});
        client.delete(url);
    }

    @Test
    public void delegatesGet() {
        context.checking(new Expectations() {{
            oneOf(delegate).get(url);
        }});
        client.get(url);
    }

    @Test
    public void delegatesGetWithHeaders() {
        context.checking(new Expectations() {{
            oneOf(delegate).get(url, headers(header("foo", "bar")));
        }});
        client.get(url, headers(header("foo", "bar")));
    }

    @Test
    public void delegatesOptions() {
        context.checking(new Expectations() {{
            oneOf(delegate).options(url);
        }});
        client.options(url);
    }

    @Test
    public void delegatesPost() {
        context.checking(new Expectations() {{
            oneOf(delegate).post(url, content);
        }});
        client.post(url, content);
    }

    @Test
    public void delegatesPut() {
        context.checking(new Expectations() {{
            oneOf(delegate).put(url, content);
        }});
        client.put(url, content);
    }

    @Test
    public void delegatesShutdown() {
        context.checking(new Expectations() {{
            oneOf(delegate).shutdown();
        }});
        client.shutdown();
    }
}
