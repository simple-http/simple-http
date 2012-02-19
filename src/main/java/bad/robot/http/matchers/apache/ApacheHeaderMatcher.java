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

import org.apache.http.Header;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class ApacheHeaderMatcher extends TypeSafeMatcher<Header> {

    private final String name;
    private final String value;

    @Factory
    public static Matcher<Header> apacheHeader(String name, String value) {
        return new ApacheHeaderMatcher(name, value);
    }

    public ApacheHeaderMatcher(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public boolean matchesSafely(Header header) {
        return header.getName().equals(name) && header.getValue().equals(value);
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(name).appendValue(value);
    }
}
