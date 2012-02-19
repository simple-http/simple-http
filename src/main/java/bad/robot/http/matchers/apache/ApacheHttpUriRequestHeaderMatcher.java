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

package bad.robot.http.matchers.apache;

import bad.robot.http.Headers;
import org.apache.http.client.methods.HttpUriRequest;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static bad.robot.http.apache.Coercions.asHeaders;
import static org.hamcrest.Matchers.is;

public class ApacheHttpUriRequestHeaderMatcher extends TypeSafeMatcher<HttpUriRequest> {

    private final Headers expected;

    @Factory
    public static Matcher<HttpUriRequest> requestContaining(Headers expected) {
        return new ApacheHttpUriRequestHeaderMatcher(expected);
    }

    public ApacheHttpUriRequestHeaderMatcher(Headers expected) {
        this.expected = expected;
    }

    @Override
    public boolean matchesSafely(HttpUriRequest actual) {
        return is(expected).matches(asHeaders(actual.getAllHeaders()));
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expected);
    }
}
