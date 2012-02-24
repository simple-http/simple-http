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

import bad.robot.http.HttpException;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.io.IOException;

import static org.hamcrest.Matchers.is;

public class ApacheHttpUriRequestContentMatcher<T extends HttpEntityEnclosingRequest> extends TypeSafeMatcher<T> {

    private final String expected;

    @Factory
    public static Matcher<? extends HttpUriRequest> messageContaining(String expected) {
        return new ApacheHttpUriRequestContentMatcher(expected);
    }

    public ApacheHttpUriRequestContentMatcher(String expected) {
        this.expected = expected;
    }

    @Override
    public boolean matchesSafely(T actual) {
        try {
            return is(expected).matches(EntityUtils.toString(actual.getEntity()));
        } catch (IOException e) {
            throw new HttpException(e);
        }
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expected);
    }
}
