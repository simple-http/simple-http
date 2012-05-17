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

import bad.robot.http.DefaultHttpResponse;
import org.hamcrest.StringDescription;
import org.junit.Test;

import static bad.robot.http.SimpleHeader.header;
import static bad.robot.http.SimpleHeaders.headers;
import static bad.robot.http.matchers.Matchers.hasHeader;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class HttpResponseHeaderStringMatcherTest {

    private final DefaultHttpResponse response = new DefaultHttpResponse(200, "OK", null, headers(
            header("Allow", "GET"),
            header("Location", "bubblegum"),
            header("Content-Length", "1024"))
    );

    @Test
    public void exampleUsage() {
        assertThat(response, hasHeader("Location", containsString("bubblegum")));
    }

    @Test
    public void matches() {
        assertThat(hasHeader("Location", containsString("bubblegum")).matches(response), is(true));
    }

    @Test
    public void doesNotMatch() {
        assertThat(hasHeader("location", containsString("bubblegum")).matches(response), is(false));
    }

    @Test
    public void description() {
        StringDescription description = new StringDescription();
        hasHeader("location", containsString("bubblegum")).describeTo(description);
        assertThat(description.toString(), containsString("header \"location\" with value of a string containing \"bubblegum\""));
    }
}
