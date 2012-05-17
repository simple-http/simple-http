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
import bad.robot.http.SimpleHeader;
import org.hamcrest.StringDescription;
import org.junit.Test;

import static bad.robot.http.SimpleHeader.header;
import static bad.robot.http.matchers.HeaderMatcher.isHeader;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class HeaderMatcherTest {

    private final SimpleHeader header = header("Accept", "application/json, q=1; text/plain, q=0.5");

    @Test
    public void exampleUsage() {
        assertThat(header, isHeader(header("Accept", "application/json, q=1; text/plain, q=0.5")));
    }

    @Test
    public void matches() {
        assertThat(isHeader(header("Accept", "application/json, q=1; text/plain, q=0.5")).matches(header), is(true));
    }

    @Test
    public void matchesAgainstHeaderImplementationWithoutEqualityImplemented() {
        assertThat(isHeader(header("Accept", "application/json, q=1; text/plain, q=0.5")).matches(new HeaderWithoutEqualityImplemented()), is(true));
    }

    @Test
    public void doesNotMatch() {
        assertThat(isHeader(header("Accept", "application/json, q=1")).matches(header), is(false));
        assertThat(isHeader(header("accept", "application/json, q=1; text/plain, q=0.5")).matches(header), is(false));
    }

    @Test
    public void description() {
        StringDescription description = new StringDescription();
        isHeader(header("Accept", "application/json, q=1; text/plain, q=0.5")).describeTo(description);
        assertThat(description.toString(), containsString("application/json, q=1; text/plain, q=0.5"));
    }

    private static class HeaderWithoutEqualityImplemented implements Header {
        @Override
        public String name() {
            return "Accept";
        }

        @Override
        public String value() {
            return "application/json, q=1; text/plain, q=0.5";
        }
    }
}
