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

package bad.robot.http.matchers;

import bad.robot.http.FormUrlEncodedMessage;
import bad.robot.http.HttpClient;
import bad.robot.http.UnencodedStringMessage;
import org.hamcrest.StringDescription;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;

import java.net.URL;

import static bad.robot.http.FormParameters.params;
import static bad.robot.http.Url.url;
import static bad.robot.http.matchers.HttpMessageContentStringMatcher.content;
import static bad.robot.http.matchers.Matchers.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class HttpMessageContentStringMatcherTest {

    private final FormUrlEncodedMessage message = new FormUrlEncodedMessage(params("first name", "bob"));

    private final Mockery context = new JUnit4Mockery();

    @Test
    public void exampleUsage() {
        assertThat(new UnencodedStringMessage("value"), content(equalTo("value")));
        assertThat(new UnencodedStringMessage("another value"), has(content(containsString("value"))));
        assertThat(new FormUrlEncodedMessage(params("json", "{}")), content(containsString("json")));
    }

    @Test
    public void anotherExample() {
        final HttpClient http = context.mock(HttpClient.class);
        context.checking(new Expectations() {{
            oneOf(http).post(with(any(URL.class)), with(post(content("post body"))));
            oneOf(http).put(with(any(URL.class)), with(put(content("put body"))));
        }});

        URL url = url("http://www.google.com");
        http.post(url, new UnencodedStringMessage("post body"));
        http.put(url, new UnencodedStringMessage("put body"));

        context.assertIsSatisfied();
    }

    @Test
    public void matches() {
        assertThat(content(equalTo("first+name=bob")).matches(message), is(true));
    }

    @Test
    public void doesNotMatch() {
        assertThat(content(equalTo("first+name=james")).matches(message), is(false));
    }

    @Test
    public void description() {
        StringDescription description = new StringDescription();
        content(equalTo("name=james")).describeTo(description);
        assertThat(description.toString(), containsString("content with \"name=james\""));
    }
}
