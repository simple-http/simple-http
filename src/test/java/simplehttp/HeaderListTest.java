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

import java.util.function.Function;

import static java.util.stream.Collectors.joining;
import static java.util.stream.StreamSupport.stream;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static simplehttp.HeaderList.headers;
import static simplehttp.HeaderPair.header;

public class HeaderListTest {

    @Test
    public void shouldConstructAListInOrder() {
        Headers headers = headers(header("a", "1"), header("b", "2"), header("c", "3"));
        
        Function<Header, String> toString = header -> header.name() + ":" + header.value();
        String actual = stream(headers.spliterator(), false).map(toString).collect(joining(" "));
        
        assertThat(actual, is("a:1 b:2 c:3"));
    }

    @Test
    public void has() {
        Headers headers = headers(header("accept", "application/json"));
        assertThat(headers.has("accept"), is(true));
        assertThat(headers.has("Accept"), is(true));
    }

    @Test
    public void hasNot() {
        Headers headers = headers(header("cheese", "ham"));
        assertThat(headers.has("accept"), is(false));
    }

    @Test
    public void getHeader() {
        Headers headers = headers(header("cheese", "ham"));
        assertThat(headers.get("cheese"), is(header("cheese", "ham")));
    }

    @Test
    public void hashCodeAndEquals() {
        EqualsVerifier.forClass(HeaderList.class).verify();
    }
}
