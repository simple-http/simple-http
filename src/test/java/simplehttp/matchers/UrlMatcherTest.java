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

package simplehttp.matchers;

import org.hamcrest.StringDescription;
import org.junit.Test;

import java.net.URL;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static simplehttp.Url.url;
import static simplehttp.matchers.UrlMatcher.containsPath;

public class UrlMatcherTest {

    private URL url  = url("http://baddotrobot.com:80/blog/archives/");

    @Test
    public void exampleUsage() {
        assertThat(url, containsPath("blog/archives"));
        assertThat(url, containsPath("baddotrobot"));
        assertThat(url, containsPath("80"));
    }

    @Test
    public void matches() {
        assertThat(containsPath("blog/archives").matches(url), is(true));
    }

    @Test
    public void doesNotMatch() {
        assertThat(containsPath("blog/2012/01").matches(url), is(false));
    }

    @Test
    public void description() {
        StringDescription description = new StringDescription();
        containsPath("blog/archives").describeTo(description);
        assertThat(description.toString(), allOf(
            containsString("a URL containing the text"),
            containsString("blog/archives"))
        );
    }

}
