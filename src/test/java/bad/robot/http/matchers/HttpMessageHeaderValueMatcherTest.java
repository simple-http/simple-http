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

import bad.robot.http.HttpPost;
import bad.robot.http.HttpResponse;
import bad.robot.http.SimpleHeader;
import bad.robot.http.UnencodedStringMessage;
import org.hamcrest.StringDescription;
import org.junit.Ignore;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static bad.robot.http.HttpClients.anApacheClient;
import static bad.robot.http.SimpleHeader.header;
import static bad.robot.http.SimpleHeaders.headers;
import static bad.robot.http.matchers.HttpMessageHeaderValueMatcher.hasHeaderWithValue;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class HttpMessageHeaderValueMatcherTest {

    private final SimpleHeader header = header("Accept", "text/html");
    private final HttpPost request = new UnencodedStringMessage("body", headers(header));

    @Test
    @Ignore("an example only")
    public void exampleUsage() throws MalformedURLException {
        HttpPost request = new UnencodedStringMessage("body", headers(header));
        HttpResponse response = anApacheClient().post(new URL("http://www.google.com"), request);

        assertThat(request, hasHeaderWithValue("Accept", containsString("html")));
        assertThat(response, hasHeaderWithValue("Content-Type", not(containsString("xml"))));
    }

    @Test
    public void matches() {
        assertThat(hasHeaderWithValue("Accept", is("text/html")).matches(request), is(true));
    }

    @Test
    public void doesNotMatch() {
        assertThat(hasHeaderWithValue("Accept", is("application/json")).matches(request), is(false));
        assertThat(hasHeaderWithValue("accept", is("text/html")).matches(header), is(false));
    }

    @Test
    public void description() {
        StringDescription description = new StringDescription();
        hasHeaderWithValue("Accept", is("application/json")).describeTo(description);
        assertThat(description.toString(), allOf(
                containsString("a HttpMessage with"),
                containsString("Accept"),
                containsString("application/json"))
        );
    }
}
