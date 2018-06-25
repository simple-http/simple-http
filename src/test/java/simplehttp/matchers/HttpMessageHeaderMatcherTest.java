/*
 * Copyright (c) 2011-2018, simple-http committers
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
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Ignore;
import org.junit.Test;
import simplehttp.*;

import java.net.URL;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static simplehttp.HeaderList.headers;
import static simplehttp.HeaderPair.header;
import static simplehttp.HttpClients.anApacheClient;
import static simplehttp.matchers.HttpMessageHeaderMatcher.has;
import static simplehttp.matchers.Matchers.put;

public class HttpMessageHeaderMatcherTest {

    private final Header header = header("Accept", "text/html");
    private final HttpPost request = new UnencodedStringMessage("body", headers(header));

    private final Mockery context = new JUnit4Mockery();

    @Test
    @Ignore ("an example only")
    public void exampleUsage() {
        HttpPost request = new UnencodedStringMessage("body", headers(header));
        HttpResponse response = anApacheClient().post(Url.url("http://www.google.com"), request);

        assertThat(request, has(header("Accept", "text/html")));
        assertThat(response, has(header("Content-Type", "tex/html")));
    }

    @Test
    public void anotherExample() {
        final HttpClient http = context.mock(HttpClient.class);
        context.checking(new Expectations() {{
            oneOf(http).put(with(any(URL.class)), with(put(has(header("Accept", "text/html")))));
        }});
        http.put(Url.url("http://www.google.com"), new UnencodedStringMessage("content", headers(header("Accept", "text/html"))));
        context.assertIsSatisfied();
    }

    @Test
    public void matches() {
        assertThat(has(header("Accept", "text/html")).matches(request), is(true));
    }

    @Test
    public void doesNotMatch() {
        assertThat(has(header("Accept", "application/json")).matches(request), is(false));
        assertThat(has(header("accept", "text/html")).matches(header), is(false));
    }

    @Test
    public void description() {
        StringDescription description = new StringDescription();
        has(header("Accept", "application/json")).describeTo(description);
        assertThat(description.toString(), allOf(
                containsString("a HttpMessage with the header"),
                containsString("Accept"),
                containsString("application/json"))
        );
    }

}
