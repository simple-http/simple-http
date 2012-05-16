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

import bad.robot.http.FormUrlEncodedMessage;
import bad.robot.http.HttpClient;
import bad.robot.http.Log4J;
import bad.robot.http.UnencodedStringMessage;
import com.google.code.tempusfugit.FactoryException;
import com.google.code.tempusfugit.temporal.Clock;
import org.apache.log4j.Logger;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import static bad.robot.http.FormParameters.params;
import static bad.robot.http.SimpleHeader.header;
import static bad.robot.http.SimpleHeaders.headers;
import static org.apache.log4j.Level.INFO;
import static org.hamcrest.Matchers.containsString;

@RunWith(JMock.class)
public class LoggingHttpClientTest {

    public static final String lineSeparator = System.getProperty("line.separator");
    private final Mockery context = new Mockery();
    private final Clock mock = new Clock() {
        @Override
        public Date create() throws FactoryException {
            return new Date();
        }
    };
    private final Logger logger = Logger.getLogger(this.getClass());
    private final Log4J log4J = Log4J.appendTo(logger, INFO);
    private final HttpClient client = context.mock(HttpClient.class);

    private final LoggingHttpClient http = new LoggingHttpClient(client, logger);

    @Test
    public void shouldLogGet() throws MalformedURLException {
        expectingHttpClientCall();
        http.get(anyUrl());
        log4J.assertThat(containsString("GET http://baddotrobot.com HTTP/1.1"));
    }

    @Test
    public void shouldLogGetWithHeaders() throws MalformedURLException {
        expectingHttpClientCall();
        http.get(anyUrl(), headers(header("Accept", "text/plain"), header("Host", "127.0.0.1")));
        log4J.assertThat(containsString("GET http://baddotrobot.com HTTP/1.1" + lineSeparator + "Accept: text/plain" + lineSeparator + "Host: 127.0.0.1"));
    }

    @Test
    public void shouldLogPostFormUrlEncoded() throws MalformedURLException {
        expectingHttpClientCall();
        http.post(anyUrl(), new FormUrlEncodedMessage(
                params(
                        "first name", "bob la rock",
                        "age", "28"
                ),
                headers(
                        header("Accept", "text/plain"),
                        header("Host", "127.0.0.1"))
        ));
        log4J.assertThat(containsString("POST http://baddotrobot.com HTTP/1.1" + lineSeparator));
        log4J.assertThat(containsString("Accept: text/plain"));
        log4J.assertThat(containsString("Host: 127.0.0.1"));
        log4J.assertThat(containsString("Content-Type: application/x-www-form-urlencoded"));
        log4J.assertThat(containsString("Content-Length: " + "first+name=bob+la+rock&age=28".getBytes().length));
        log4J.assertThat(containsString("first+name=bob+la+rock&age=28"));
    }

    @Test
    public void shouldLogPostUnencoded() throws MalformedURLException {
        expectingHttpClientCall();
        http.post(anyUrl(), new UnencodedStringMessage("cheese sandwich", headers(header("Accept", "text/plain"), header("Host", "127.0.0.1"))));
        log4J.assertThat(containsString("POST http://baddotrobot.com HTTP/1.1"));
        log4J.assertThat(containsString("Accept: text/plain"));
        log4J.assertThat(containsString("Host: 127.0.0.1"));
        log4J.assertThat(containsString("cheese sandwich"));
    }

    @Test
    public void shouldLogPut() throws MalformedURLException {
        expectingHttpClientCall();
        http.put(anyUrl(), new UnencodedStringMessage("cheese sandwich", headers(header("Accept", "text/plain"), header("Host", "127.0.0.1"))));
        log4J.assertThat(containsString("PUT http://baddotrobot.com HTTP/1.1"));
        log4J.assertThat(containsString("Accept: text/plain"));
        log4J.assertThat(containsString("Host: 127.0.0.1"));
        log4J.assertThat(containsString("cheese sandwich"));
    }

    @Test
    public void shouldLogDelete() throws MalformedURLException {
        expectingHttpClientCall();
        http.delete(anyUrl());
        log4J.assertThat(containsString("DELETE http://baddotrobot.com HTTP/1.1"));
    }

    private void expectingHttpClientCall() {
        context.checking(new Expectations() {{
            one(client);
        }});
    }

    private URL anyUrl() throws MalformedURLException {
        return new URL("http://baddotrobot.com");
    }

    @After
    public void cleanupLog4J() {
        log4J.clean();
    }
}
