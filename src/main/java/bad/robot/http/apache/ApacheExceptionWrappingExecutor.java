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

package bad.robot.http.apache;

import bad.robot.http.*;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;

public class ApacheExceptionWrappingExecutor implements Executor<HttpException> {

    @Override
    public <V> V submit(Callable<V> callable) throws HttpException {
        try {
            return callable.call();
        } catch (ConnectTimeoutException e) {
            throw new HttpConnectionTimeoutException(e);
        } catch (SocketTimeoutException e) {
            throw new HttpSocketTimeoutException(e);
        } catch (HttpHostConnectException e) {
            throw new HttpConnectionRefusedException(e);
        } catch (UnknownHostException e) {
            throw new HttpUnknownHostException(e.getMessage(), e);
        } catch (Throwable e) {
            throw new HttpException(e);
        }
    }
}
