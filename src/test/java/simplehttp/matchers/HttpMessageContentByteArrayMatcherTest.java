/*
 * Copyright (c) 2011-2013, bad robot (london) ltd
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

package simplehttp.matchers;

import org.hamcrest.StringDescription;
import org.junit.Test;
import simplehttp.HttpResponse;
import simplehttp.StringHttpResponse;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static simplehttp.HeaderList.headers;
import static simplehttp.HeaderPair.header;
import static simplehttp.matchers.Matchers.binaryContent;
import static simplehttp.matchers.Matchers.has;

public class HttpMessageContentByteArrayMatcherTest {

    private final HttpResponse response = new StringHttpResponse(200, "OK", "expected body", headers(header("Accept", "application/json")), "http://example.com");
    private final byte[] expected = "expected body".getBytes();

    @Test
    public void exampleUsage() {
        assertThat(response, has(binaryContent(expected)));
        assertThat(response, has(binaryContent(equalTo(expected))));
        assertThat(response, has(binaryContent(equalTo(expected), "UTF-8")));
    }

    @Test
    public void matches() {
        assertThat(binaryContent(equalTo(expected)).matches(response), is(true));
    }

    @Test
    public void doesNotMatch() {
        assertThat(binaryContent(equalTo("not expected".getBytes())).matches(response), is(false));
    }

    @Test (expected = RuntimeException.class)
    public void unsupportedEncodingThrowsException() {
        binaryContent(equalTo(expected), "Marmite-Land").matches(response);
    }

    @Test
    public void description() {
        StringDescription description = new StringDescription();
        binaryContent(equalTo(expected)).describeTo(description);
        assertThat(description.toString(), allOf(
            containsString("a HttpMessage with binary content"),
            containsString("<101>, <120>, <112>, <101>, <99>, <116>, <101>, <100>, <32>, <98>, <111>, <100>, <121>"))
        );
    }
}
