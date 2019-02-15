/*
 * Copyright (c) 2011-2018, simple-http committers
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

package simplehttp.apache.matchers;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.hamcrest.*;

import java.util.Optional;
import java.util.function.Function;

import static org.apache.http.impl.client.ReflectiveInternalHttpClientAdapter.getConfig;
import static org.hamcrest.Matchers.is;

public class InternalHttpClientConfigMatcher extends TypeSafeDiagnosingMatcher<HttpClient> {

	private final RequestConfig expected;

	@Factory
	public static InternalHttpClientConfigMatcher hasConfiguration(RequestConfig expected) {
		return new InternalHttpClientConfigMatcher(expected);
	}

	public InternalHttpClientConfigMatcher(RequestConfig expected) {
		this.expected = expected;
	}

	/** Because {@link RequestConfig} doesn't implement equality, resorting to #toString for comparison, yuk. */
	@Override
	protected boolean matchesSafely(HttpClient client, Description mismatch) {
		return getConfig(client, mismatch).map(actual -> {
			boolean match = actual.toString().equals(expected.toString());
			if (!match)
				mismatch.appendText("was ").appendValue(actual.toString());
			return match;
		}).orElse(false);
	}

	@Override
	public void describeTo(Description description) {
		description.appendValue(expected.toString()).appendText(" expected ");
	}

}
