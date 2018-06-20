/*
 * Copyright (c) 2011-2013, bad robot (london) ltd
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

package simplehttp.apache;

import org.apache.http.HttpHost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import simplehttp.*;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;

import static org.hamcrest.Matchers.instanceOf;

public class ApacheExceptionWrappingExecutorTest {

    @Rule public final ExpectedException exception = ExpectedException.none();

    private final ApacheExceptionWrappingExecutor wrapper = new ApacheExceptionWrappingExecutor();

    @Test
    public void wrapsConnectTimeoutException() {
        exception.expect(HttpConnectionTimeoutException.class);
        exception.expect(new ThrowableCauseMatcher(ConnectTimeoutException.class));
        wrapper.submit(throwsException(new ConnectTimeoutException()));
    }

    @Test
    public void wrapsSocketTimeoutException() {
        exception.expect(HttpSocketTimeoutException.class);
        exception.expect(new ThrowableCauseMatcher(SocketTimeoutException.class));
        wrapper.submit(throwsException(new SocketTimeoutException()));
    }

    @Test
    public void wrapsConnectException() {
        exception.expect(HttpConnectionRefusedException.class);
        exception.expect(new ThrowableCauseMatcher(HttpHostConnectException.class));
        wrapper.submit(throwsException(new HttpHostConnectException(new HttpHost("localhost"), new ConnectException())));
    }

    @Test
    public void wrapsUnknownHostException() {
        exception.expect(HttpUnknownHostException.class);
        exception.expect(new ThrowableCauseMatcher(UnknownHostException.class));
        wrapper.submit(throwsException(new UnknownHostException("cheese")));
    }

    @Test
    public void wrapsException() {
        exception.expect(HttpException.class);
        exception.expect(new ThrowableCauseMatcher(Exception.class));
        wrapper.submit(throwsException(new Exception()));
    }

    private static Callable<Object> throwsException(final Exception e) {
        return new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                throw e;
            }
        };
    }

    private static class ThrowableCauseMatcher extends TypeSafeMatcher<Throwable> {
        private final Class<?> cause;

        public ThrowableCauseMatcher(Class<?> cause) {
            this.cause = cause;
        }

        @Override
        protected boolean matchesSafely(Throwable actual) {
            return instanceOf(cause).matches(actual.getCause());
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("with cause ");
            instanceOf(cause).describeTo(description);
        }
    }
}
