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

import bad.robot.http.HttpResponse;
import bad.robot.http.StringHttpResponse;
import org.hamcrest.StringDescription;
import org.junit.Test;

import static bad.robot.http.HeaderList.headers;
import static bad.robot.http.HeaderPair.header;
import static bad.robot.http.matchers.HttpResponseStatusCodeMatcher.status;
import static bad.robot.http.matchers.Matchers.has;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class HttpResponseStatusCodeMatcherTest {

    private final HttpResponse response = new StringHttpResponse(200, "OK", "{ \"message\", \"not found\" }", headers(header("Content-Type", "application/json")), "http://example.com");

    @Test
    public void exampleUsage() {
        assertThat(response, has(status(200)));
    }

    @Test
    public void matches() {
        assertThat(status(200).matches(response), is(true));
    }

    @Test
    public void doesNotMatch() {
        assertThat(status(201).matches(response), is(false));
    }

    @Test
    public void description() {
        StringDescription description = new StringDescription();
        status(200).describeTo(description);
        assertThat(description.toString(), allOf(
            containsString("a HttpMessage with status code"),
            containsString("200"))
        );
    }

}
