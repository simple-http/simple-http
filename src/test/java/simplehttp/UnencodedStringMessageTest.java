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

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static simplehttp.CharacterSet.defaultCharacterSet;
import static simplehttp.EmptyHeaders.emptyHeaders;
import static simplehttp.HeaderList.headers;
import static simplehttp.HeaderPair.header;

public class UnencodedStringMessageTest {

	@Test
	public void shouldContainContent() {
		assertThat(new UnencodedStringMessage("content").getContent(), is(new StringMessageContent("content")));
		assertThat(new UnencodedStringMessage("content", emptyHeaders()).getContent(), is(new StringMessageContent("content")));
		assertThat(new UnencodedStringMessage("content", emptyHeaders(), defaultCharacterSet).getContent(), is(new StringMessageContent("content")));
		assertThat(new UnencodedStringMessage("content", defaultCharacterSet).getContent(), is(new StringMessageContent("content")));
	}

	@Test
	public void shouldContainHeaders() {
		assertThat(new UnencodedStringMessage("n/a", emptyHeaders()).getHeaders(), is(emptyHeaders()));
		assertThat(new UnencodedStringMessage("n/a", defaultCharacterSet).getHeaders(), is(emptyHeaders()));
		assertThat(new UnencodedStringMessage("n/a", emptyHeaders(), defaultCharacterSet).getHeaders(), is(emptyHeaders()));
	}

	@Test
	public void stringRepresentation() {
		assertThat(new UnencodedStringMessage("content").toString(), is("UnencodedStringMessage{content='content', headers=''}"));
		assertThat(new UnencodedStringMessage("content", headers(header("a", "b"))).toString(), is("UnencodedStringMessage{content='content', headers='HeaderList{headers=[HeaderPair{name='a', value='b'}]}'}"));
		assertThat(new UnencodedStringMessage("content", headers(header("a", "b")), CharacterSet.UTF_8).toString(), is("UnencodedStringMessage{content='content', headers='HeaderList{headers=[HeaderPair{name='a', value='b'}]}'}"));
	}

	@Test
	public void hashCodeAndEquals() {
		EqualsVerifier.forClass(UnencodedStringMessage.class).verify();
	}
}