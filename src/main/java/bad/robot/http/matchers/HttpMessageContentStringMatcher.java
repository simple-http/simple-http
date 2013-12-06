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

import bad.robot.http.HttpMessage;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static org.hamcrest.Matchers.equalTo;

class HttpMessageContentStringMatcher<T extends HttpMessage> extends TypeSafeMatcher<T> {

    private final Matcher<String> matcher;

    @Factory
    public static <T extends HttpMessage> Matcher<T> content(Matcher<String> matcher) {
        return new HttpMessageContentStringMatcher<T>(matcher);
    }

    @Factory
    public static <T extends HttpMessage> Matcher<T> content(String content) {
        return new HttpMessageContentStringMatcher<T>(equalTo(content));
    }

    public HttpMessageContentStringMatcher(Matcher<String> matcher) {
        this.matcher = matcher;
    }

    @Override
    public boolean matchesSafely(T actual) {
        return matcher.matches(actual.getContent().asString());
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("content with ");
        matcher.describeTo(description);
    }
}
