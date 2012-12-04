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

package bad.robot.http.matchers;

import bad.robot.http.Headers;
import org.hamcrest.StringDescription;
import org.junit.Test;

import static bad.robot.http.HeaderList.headers;
import static bad.robot.http.HeaderPair.header;
import static bad.robot.http.matchers.HeadersEqualityMatcher.hasHeader;
import static bad.robot.http.matchers.HeadersEqualityMatcher.hasHeaders;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class HeadersEqualityMatcherTest {

    private final Headers headers = headers(header("Accept", "application/json"), header("Accept-Language", "en_UK"));

    @Test
    public void exampleUsage() {
        assertThat(headers, hasHeader(header("Accept", "application/json")));
    }

    @Test
    public void matches() {
        assertThat(hasHeader(header("Accept", "application/json")).matches(headers), is(true));
        assertThat(hasHeader(header("Accept-Language", "en_UK")).matches(headers), is(true));
        assertThat(hasHeaders(header("Accept", "application/json"), header("Accept-Language", "en_UK")).matches(headers), is(true));
    }

    @Test
    public void doesNotMatch() {
        assertThat(hasHeaders(header("Accept", "application/xml")).matches(headers), is(false));
        assertThat(hasHeaders(header("Accept", "application/json"), header("Accept-Language", "UK")).matches(headers), is(false));
        assertThat(hasHeaders(header("Accept-Language", "UK"), header("Accept", "application/json")).matches(headers), is(false));
    }

    @Test
    public void description() {
        StringDescription description = new StringDescription();
        hasHeaders(header("Content-Length", "1024"), header("Content-Type", "text/plain")).describeTo(description);
        assertThat(description.toString(), allOf(
                containsString("headers to contain (all items in any order)"),
                containsString("name='Content-Length', value='1024'"),
                containsString("name='Content-Type', value='text/plain'")));
    }
}
