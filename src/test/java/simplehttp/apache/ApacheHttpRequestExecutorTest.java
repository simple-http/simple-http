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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.protocol.HttpContext;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Test;
import simplehttp.HttpResponse;
import simplehttp.StringHttpResponse;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static simplehttp.EmptyHeaders.emptyHeaders;

public class ApacheHttpRequestExecutorTest {

	private final JUnitRuleMockery context = new JUnitRuleMockery();
	
	private final HttpContext localContext = context.mock(HttpContext.class);
	private final HttpClient client = context.mock(HttpClient.class);

	private final HttpGet anyRequest = new HttpGet("http://whatever.trevor");
	private final HttpResponse anyResponse = new StringHttpResponse(200, "OK", "", emptyHeaders(), "http://example.com");

	@Test
	public void delegates() throws Exception {
		context.checking(new Expectations() {{
			oneOf(client).execute(with(anyRequest), with(instanceOf(HttpResponseHandler.class)), with(localContext));
			will(returnValue(anyResponse));
		}});
		assertThat(new ApacheHttpRequestExecutor(client, localContext, anyRequest).call(), is(anyResponse));
	}

	private static Matcher<ResponseHandler> instanceOf(Class<HttpResponseHandler> type) {
		return Matchers.instanceOf(type);
	}
}
