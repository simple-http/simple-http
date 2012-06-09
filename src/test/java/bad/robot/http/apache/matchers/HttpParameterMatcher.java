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

package bad.robot.http.apache.matchers;

import org.apache.http.client.HttpClient;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class HttpParameterMatcher extends TypeSafeDiagnosingMatcher<HttpClient> {

    private final String parameter;
    private final Matcher<?> matcher;

    public HttpParameterMatcher(String parameter, Matcher<?> matcher) {
        this.parameter = parameter;
        this.matcher = matcher;
    }

    public static HttpParameterMatcher parameter(String parameter, Matcher<?> matcher) {
        return new HttpParameterMatcher(parameter, matcher);
    }

    @Override
    protected boolean matchesSafely(HttpClient actual, Description mismatchDescription) {
        Object parameter = actual.getParams().getParameter(this.parameter);
        mismatchDescription.appendText(" actual ").appendValue(parameter != null ? parameter.toString() : "<null>");
        return matcher.matches(parameter);
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(parameter).appendText(" expected ");
        matcher.describeTo(description);
    }

}
