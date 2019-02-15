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

package simplehttp;

import org.junit.Test;

import java.io.File;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static simplehttp.EmptyHeaders.emptyHeaders;

public class MultipartTest {

	@Test
	public void shouldContainContent() {
		assertThat(new Multipart("example", new File("dunno.txt")).getContent(), is(new MultipartContent("example", new File("dunno.txt"))));
	}

	@Test
	public void shouldContainHeaders() {
		assertThat(new Multipart("example", new File("dunno.txt")).getHeaders(), is(emptyHeaders()));
	}

	@Test
	public void stringRepresentation() {
		assertThat(new Multipart("example", new File("dunno.txt")).toString(), is("Multipart{content='name=example, filename=dunno.txt', headers=''}"));
	}

}