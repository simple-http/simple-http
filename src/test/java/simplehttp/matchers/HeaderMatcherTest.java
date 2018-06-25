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

import org.hamcrest.StringDescription;
import org.junit.Test;
import simplehttp.Header;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static simplehttp.HeaderPair.header;
import static simplehttp.matchers.Matchers.header;

public class HeaderMatcherTest {

    private final Header header = header("Accept", "application/json, q=1; text/plain, q=0.5");

    @Test
    public void exampleUsage() {
        assertThat(header, header("Accept", containsString("json")));
    }

    @Test
    public void matches() {
        assertThat(header("Accept", containsString("json")).matches(header), is(true));
    }

    @Test
    public void doesNotMatch() {
        assertThat(header("Accept", containsString("xml")).matches(header), is(false));
        assertThat(header("accept", containsString("json")).matches(header), is(false));
    }

    @Test
    public void description() {
        StringDescription description = new StringDescription();
        header("Accept", equalTo("json")).describeTo(description);
        assertThat(description.toString(), containsString("header \"Accept\" with value of \"json\""));
    }

}
