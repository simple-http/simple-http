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
import bad.robot.http.HttpResponse;
import org.hamcrest.StringDescription;
import org.junit.Test;

import java.net.MalformedURLException;

import static bad.robot.http.SimpleHeader.header;
import static bad.robot.http.SimpleHeaders.headers;
import static bad.robot.http.matchers.HttpResponseStatusMessageMatcher.statusMessage;
import static bad.robot.http.matchers.Matchers.has;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class HttpResponseStatusMessageMatcherTest {

    private final HttpResponse response = new DefaultHttpResponse(200, "OK", null, headers(header("Accept", "application/json")));

    @Test
    public void exampleUsage() throws MalformedURLException {
        assertThat(response, has(statusMessage("OK")));
        assertThat(response, has(statusMessage(equalTo("OK"))));
    }

    @Test
    public void matches() {
        assertThat(statusMessage(equalTo("OK")).matches(response), is(true));
    }

    @Test
    public void doesNotMatch() {
        assertThat(statusMessage(equalTo("Not Found")).matches(response), is(false));
        assertThat(statusMessage(equalTo("ok")).matches(response), is(false));
    }

    @Test
    public void description() {
        StringDescription description = new StringDescription();
        statusMessage(equalTo("OK")).describeTo(description);
        assertThat(description.toString(), allOf(
            containsString("a HttpMessage with status message"),
            containsString("OK"))
        );
    }

}
