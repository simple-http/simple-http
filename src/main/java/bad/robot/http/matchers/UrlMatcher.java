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

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.net.URL;

public class UrlMatcher extends TypeSafeMatcher<URL> {

    private final String path;

    @Factory
    public static Matcher<URL> containsPath(String path) {
        return new UrlMatcher(path);
    }

    public UrlMatcher(String path) {
        this.path = path;
    }

    @Override
    public boolean matchesSafely(URL url) {
        return getFullPathFrom(url).contains(path);
    }

    private static String getFullPathFrom(URL url) {
        StringBuilder builder = new StringBuilder(url.getProtocol()).append("://").append(url.getHost());
        if (portExists(url))
            builder.append(":").append(url.getPort());
        builder.append(url.getPath());
        if (queryExists(url))
            builder.append("?").append(url.getQuery()).toString();
        return builder.toString();
    }

    private static boolean queryExists(URL url) {
        return url.getQuery() != null;
    }

    private static boolean portExists(URL url) {
        return url.getPort() != -1;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("with a URL containing the text ").appendValue(path);
    }
}