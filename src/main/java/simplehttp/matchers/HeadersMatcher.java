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

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.collection.IsIterableContainingInOrder;
import simplehttp.Header;
import simplehttp.Headers;

import java.util.List;

import static java.lang.String.format;
import static java.lang.System.getProperty;
import static java.util.Arrays.asList;
import static simplehttp.matchers.Matchers.expectedLineLeadingSpaces;

class HeadersMatcher extends TypeSafeMatcher<Headers> {

    private final List<Matcher<Header>> matchers;

    @Factory
    public static Matcher<Headers> hasHeaders(Matcher<Header>... headers) {
        return new HeadersMatcher(asList(headers));
    }

    private HeadersMatcher(List<Matcher<Header>> matchers) {
        this.matchers = matchers;
    }

    @Override
    public boolean matchesSafely(Headers actual) {
        return new IsIterableContainingInOrder(matchers).matches(actual);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("headers to contain (all items in any order)")
                .appendText(getProperty("line.separator"))
                .appendText(expectedLineLeadingSpaces())
                .appendList("", format(" and %s%s", getProperty("line.separator"), expectedLineLeadingSpaces()), "", matchers);
    }

}
