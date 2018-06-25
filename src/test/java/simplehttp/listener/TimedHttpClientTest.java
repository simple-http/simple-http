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

package simplehttp.listener;

import com.google.code.tempusfugit.FactoryException;
import com.google.code.tempusfugit.temporal.Clock;
import org.apache.log4j.Logger;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import simplehttp.HttpClient;
import simplehttp.Log4J;
import simplehttp.StringHttpResponse;

import java.io.IOException;
import java.net.URL;
import java.util.Date;

import static com.google.code.tempusfugit.temporal.Duration.millis;
import static org.apache.log4j.Level.INFO;
import static org.hamcrest.Matchers.containsString;
import static simplehttp.Any.anyUrl;
import static simplehttp.EmptyHeaders.emptyHeaders;
import static simplehttp.listener.TimedHttpClient.timedHttpClient;

@RunWith(JMock.class)
public class TimedHttpClientTest {

    private final Mockery context = new Mockery();
    private final Class<? extends TimedHttpClientTest> logger = this.getClass();

    private final Clock clock = context.mock(Clock.class);
    private final HttpClient delegate = context.mock(HttpClient.class);
    private final Log4J log4J = Log4J.appendTo(Logger.getLogger(logger), INFO);

    @Test
    public void shouldDelegate() {
        final URL url = anyUrl();
        context.checking(new Expectations() {{
            oneOf(delegate).get(url);
        }});
        timedHttpClient(delegate, new FixedClock(), logger).get(url);
    }

    @Test
    public void shouldTimeRequest() throws IOException {
        context.checking(new Expectations() {{
            allowing(delegate).get(with(any(URL.class)));
            oneOf(clock).create(); will(returnValue(new Date(0)));
            oneOf(clock).create(); will(returnValue(new Date(millis(100).inMillis())));
        }});
        timedHttpClient(delegate, clock, logger).get(anyUrl());
        log4J.assertThat(containsString("100 MILLISECONDS"));
    }

    @Test
    public void shouldLogDetails() {
        context.checking(new Expectations() {{
            allowing(delegate).get(with(any(URL.class))); will(returnValue(new StringHttpResponse(200, "OK", "nothing", emptyHeaders(), "http://example.com")));
        }});
        timedHttpClient(delegate, new FixedClock(), logger).get(anyUrl());
        log4J.assertThat(containsString("GET http://not.real.url was 200 (OK), took Duration 0 MILLISECONDS"));
    }

    @Test (expected = IllegalArgumentException.class)
    public void shouldNotAllowVagueLoggerClass() throws IOException {
        timedHttpClient(delegate, clock, Object.class).get(anyUrl());
    }

    @After
    public void cleanupLog4J() {
        log4J.clean();
    }

    private static class FixedClock implements Clock {
        @Override
        public Date create() throws FactoryException {
            return new Date(0);
        }
    }

}
