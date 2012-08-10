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

import bad.robot.http.DefaultHttpResponse;
import bad.robot.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.protocol.HttpContext;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import static bad.robot.http.EmptyHeaders.emptyHeaders;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(JMock.class)
public class ApacheHttpRequestExecutorTest {

    private final Mockery context = new JUnit4Mockery();
    private final HttpContext localContext = context.mock(HttpContext.class);
    private final HttpClient client = context.mock(HttpClient.class);

    private final HttpGet anyRequest = new HttpGet("http://whatever.trevor");
    private final HttpResponse anyResponse = new DefaultHttpResponse(200, "OK", "", emptyHeaders());

    @Test
    public void delegates() throws Exception {
        context.checking(new Expectations() {{
            oneOf(client).execute(with(anyRequest), with(instanceOf(HttpResponseHandler.class)), with(localContext)); will(returnValue(anyResponse));
        }});
        assertThat(new ApacheHttpRequestExecutor(client, localContext, anyRequest).call(), is(anyResponse));
    }

    private static Matcher<ResponseHandler> instanceOf(Class<HttpResponseHandler> type) {
        return Matchers.instanceOf(type);
    }
}
