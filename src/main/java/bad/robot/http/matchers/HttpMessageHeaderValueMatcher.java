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

import bad.robot.http.Header;
import bad.robot.http.HttpMessage;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

class HttpMessageHeaderValueMatcher<T extends HttpMessage> extends TypeSafeMatcher<T> {

    private final HeaderValueMatcher delegate;

    @Factory
    public static <T extends HttpMessage> Matcher<T> headerWithValue(String name, Matcher<String> matcher) {
        return new HttpMessageHeaderValueMatcher(name, matcher);
    }

    public HttpMessageHeaderValueMatcher(String name, Matcher<String> matcher) {
        this.delegate = HeaderValueMatcher.hasHeaderWithValue(name, matcher);
    }

    @Override
    public boolean matchesSafely(T actual) {
        for (Header header : actual.getHeaders()) {
            if (delegate.matches(header))
                return true;
        }
        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a ").appendText(HttpMessage.class.getSimpleName()).appendText(" with ");
        delegate.describeTo(description);
    }
}
