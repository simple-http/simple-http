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

package bad.robot.http.apache;

import bad.robot.http.SimpleHeaders;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.junit.Test;

import static bad.robot.http.SimpleHeader.header;
import static bad.robot.http.SimpleHeaders.headers;
import static bad.robot.http.matchers.Matchers.apacheHeader;
import static bad.robot.http.matchers.Matchers.has;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertThat;

public class CoercionsTest {

    private final Header[] apacheHeaders = new Header[]{new BasicHeader("Accept", "text/html"), new BasicHeader("Location", "http://baddotrobot.com")};
    private final SimpleHeaders simpleHttpHeaders = headers(header("Accept", "text/html"), header("Location", "http://baddotrobot.com"));

    @Test
    public void covertToApacheHeaders() {
        Header[] headers = Coercions.asApacheBasicHeader(simpleHttpHeaders);
        assertThat(asList(headers), hasItems(apacheHeader("Accept", "text/html"), apacheHeader("Location", "http://baddotrobot.com")));
    }

    @Test
    public void convertFromApacheHeaders() {
        assertThat(Coercions.asHeaders(apacheHeaders), has(header("Accept", "text/html"), header("Location", "http://baddotrobot.com")));
    }
}
