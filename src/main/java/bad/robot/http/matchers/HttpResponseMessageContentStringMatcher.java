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
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class HttpResponseMessageContentStringMatcher extends TypeSafeMatcher<HttpResponse> {

    private final Matcher<String> matcher;

    @Factory
    public static HttpResponseMessageContentStringMatcher hasContent(Matcher<String> matcher) {
        return new HttpResponseMessageContentStringMatcher(matcher);
    }

    public HttpResponseMessageContentStringMatcher(Matcher<String> matcher) {
        this.matcher = matcher;
    }

    @Override
    public boolean matchesSafely(HttpResponse actual) {
        return matcher.matches(actual.getContent().asString());
    }

    @Override
    public void describeTo(Description description) {
        matcher.describeTo(description);
    }
}
