/*
 * Copyright (c) 2011-2019, simple-http committers
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

package simplehttp.listener;

import org.apache.log4j.Logger;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Test;
import simplehttp.HttpClient;
import simplehttp.Log4J;
import simplehttp.StringHttpResponse;

import java.net.URL;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static java.time.Instant.ofEpochMilli;
import static java.time.ZoneId.of;
import static org.apache.log4j.Level.INFO;
import static org.hamcrest.Matchers.containsString;
import static simplehttp.Any.anyUrl;
import static simplehttp.EmptyHeaders.emptyHeaders;
import static simplehttp.listener.TimedHttpClient.timedHttpClient;

public class TimedHttpClientTest {

    private final JUnitRuleMockery context = new JUnitRuleMockery();
    private final Class<? extends TimedHttpClientTest> logger = this.getClass();

    private final HttpClient delegate = context.mock(HttpClient.class);
    private final Log4J log4J = Log4J.appendTo(Logger.getLogger(logger), INFO);
    private final Clock fixedClock = Clock.fixed(ofEpochMilli(0), of("GMT"));

    @Test
    public void shouldDelegate() {
        final URL url = anyUrl();
        context.checking(new Expectations() {{
            oneOf(delegate).get(url);
        }});
        timedHttpClient(delegate, fixedClock, logger).get(url);
    }

    @Test
    public void shouldTimeRequest() {
        Clock clock = new FixedIncrementClock(100);
        context.checking(new Expectations() {{
            allowing(delegate).get(with(any(URL.class))); will(returnValue(new StringHttpResponse(200, "OK", "nothing", emptyHeaders(), "http://example.com")));
        }});
        timedHttpClient(delegate, clock, logger).get(anyUrl());
        log4J.assertThat(containsString("took 100ms"));
    }

    @Test
    public void shouldLogDetails() {
        context.checking(new Expectations() {{
            allowing(delegate).get(with(any(URL.class))); will(returnValue(new StringHttpResponse(200, "OK", "nothing", emptyHeaders(), "http://example.com")));
        }});
        timedHttpClient(delegate, fixedClock, logger).get(anyUrl());
        log4J.assertThat(containsString("GET http://not.real.url was 200 (OK), took 0ms"));
    }

    @Test (expected = IllegalArgumentException.class)
    public void shouldNotAllowVagueLoggerClass() {
        timedHttpClient(delegate, fixedClock, Object.class).get(anyUrl());
    }

    @After
    public void cleanupLog4J() {
        log4J.clean();
    }

    private static class FixedIncrementClock extends Clock {
        private long now = 0;
        private long increment;

        private FixedIncrementClock(long increment) {
            this.increment = increment;
        }

        @Override
        public ZoneId getZone() {
            return null;
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return null;
        }

        @Override
        public Instant instant() {
            now = now + increment;
            return Instant.ofEpochMilli(now);
        }
    }
}
