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

package bad.robot.http.listener;

import bad.robot.http.DefaultHttpResponse;
import bad.robot.http.HttpClient;
import bad.robot.http.Log4J;
import bad.robot.http.Url;
import com.google.code.tempusfugit.FactoryException;
import com.google.code.tempusfugit.temporal.Clock;
import org.apache.log4j.Logger;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Date;

import static bad.robot.http.SimpleHeaders.noHeaders;
import static bad.robot.http.listener.TimedHttpClient.timedHttpClient;
import static com.google.code.tempusfugit.temporal.Duration.millis;
import static org.apache.log4j.Level.INFO;
import static org.hamcrest.Matchers.containsString;

@RunWith(JMock.class)
public class TimedHttpClientTest {

    private final Mockery context = new Mockery();
    private final Class<? extends TimedHttpClientTest> logger = this.getClass();

    private final Clock clock = context.mock(Clock.class);
    private final HttpClient delegate = context.mock(HttpClient.class);
    private final Log4J log4J = Log4J.appendTo(Logger.getLogger(logger), INFO);

    @Test
    public void shouldDelegate() {
        final Url url = anyUrl();
        context.checking(new Expectations() {{
            one(delegate).get(url);
        }});
        timedHttpClient(delegate, new FixedClock(), logger).get(url);
    }

    @Test
    public void shouldTimeRequest() throws IOException {
        context.checking(new Expectations() {{
            allowing(delegate).get(with(any(Url.class)));
            one(clock).create(); will(returnValue(new Date(0)));
            one(clock).create(); will(returnValue(new Date(millis(100).inMillis())));
        }});
        timedHttpClient(delegate, clock, logger).get(anyUrl());
        log4J.assertThat(containsString("100 MILLISECONDS"));
    }

    @Test
    public void shouldLogDetails() {
        context.checking(new Expectations() {{
            allowing(delegate).get(with(any(Url.class))); will(returnValue(new DefaultHttpResponse(200, "OK", "nothing", noHeaders())));
        }});
        timedHttpClient(delegate, new FixedClock(), logger).get(anyUrl());
        log4J.assertThat(containsString("GET http://whatever was 200 (OK), took Duration 0 MILLISECONDS"));
    }

    @Test (expected = IllegalArgumentException.class)
    public void shouldNotAllowVagueLoggerClass() throws IOException {
        timedHttpClient(delegate, clock, Object.class).get(anyUrl());
    }

    private static Url anyUrl() {
        return new Url("http://whatever");
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
