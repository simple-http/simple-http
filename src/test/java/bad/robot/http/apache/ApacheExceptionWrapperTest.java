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

import bad.robot.http.HttpConnectionTimeoutException;
import bad.robot.http.HttpSocketTimeoutException;
import org.apache.http.conn.ConnectTimeoutException;
import org.junit.Test;

import java.net.SocketTimeoutException;
import java.util.concurrent.Callable;

public class ApacheExceptionWrapperTest {

    private final ApacheExceptionWrapper wrapper = new ApacheExceptionWrapper();

    @Test (expected = HttpConnectionTimeoutException.class)
    public void wrapsConnectTimeoutException() {
        wrapper.execute(throwsException(new ConnectTimeoutException()));
    }

    @Test (expected = HttpSocketTimeoutException.class)
    public void wrapsSocketTimeoutException() {
        wrapper.execute(throwsException(new SocketTimeoutException()));
    }
    
    private static Callable<Object> throwsException(final Exception e) {
        return new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                throw e;
            }
        };
    }
}
