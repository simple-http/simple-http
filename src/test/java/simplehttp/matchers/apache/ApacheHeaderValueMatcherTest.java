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

package simplehttp.matchers.apache;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.hamcrest.StringDescription;
import org.junit.Test;
import simplehttp.matchers.Matchers;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class ApacheHeaderValueMatcherTest {

	private final Header header = new BasicHeader("Accept", "application/json, q=1; text/plain, q=0.5");

	@Test
	public void exampleUsage() {
		assertThat(header, is(Matchers.apacheHeader("Accept", is("application/json, q=1; text/plain, q=0.5"))));
	}

	@Test
	public void matches() {
		assertThat(Matchers.apacheHeader("Accept", is("application/json, q=1; text/plain, q=0.5")).matches(header), is(true));
		assertThat(Matchers.apacheHeader("Accept", is(not("application/json, q=1; text/plain, q=1.0"))).matches(header), is(true));
	}

	@Test
	public void doesNotMatch() {
		assertThat(Matchers.apacheHeader("Accept", containsString("xml")).matches(header), is(false));
		assertThat(Matchers.apacheHeader("accept", is("application/json, q=1; text/plain, q=0.5")).matches(header), is(false));
	}

	@Test
	public void description() {
		StringDescription description = new StringDescription();
		Matchers.apacheHeader("Accept", is("application/json, q=1; text/plain, q=0.5")).describeTo(description);
		assertThat(description.toString(), is("header \"Accept\" with matcher is \"application/json, q=1; text/plain, q=0.5\""));
	}
	
	@Test
	public void descriptionWhenMismatch() {
		StringDescription description = new StringDescription();
		Matchers.apacheHeader("Accept", is(not("application/json, q=1; text/plain, q=0.5"))).describeTo(description);
		assertThat(description.toString(), is("header \"Accept\" with matcher is not \"application/json, q=1; text/plain, q=0.5\""));
	}
}