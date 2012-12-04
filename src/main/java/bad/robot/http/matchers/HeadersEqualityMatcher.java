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
import bad.robot.http.Headers;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.Arrays;
import java.util.List;

import static bad.robot.http.matchers.Matchers.expectedLineLeadingSpaces;
import static java.lang.System.getProperty;

class HeadersEqualityMatcher extends TypeSafeMatcher<Headers> {

    private final List<Header> expected;

    @Factory
    public static Matcher<Headers> hasHeaders(Header... headers) {
        return new HeadersEqualityMatcher(headers);
    }

    @Factory
    public static Matcher<Headers> hasHeader(Header header) {
        return new HeadersEqualityMatcher(new Header[] {header});
    }

    private HeadersEqualityMatcher(Header[] expected) {
        this.expected = Arrays.asList(expected);
    }

    @Override
    public boolean matchesSafely(Headers actual) {
        int matches = 0;
        for (Header header : actual)
            if (expected.contains(header))
                matches++;
        return matches == expected.size();
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("headers to contain (all items in any order)")
                .appendText(getProperty("line.separator"))
                .appendText(expectedLineLeadingSpaces())
                .appendValue(expected);
    }
}
