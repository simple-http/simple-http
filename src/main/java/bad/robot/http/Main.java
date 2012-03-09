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

package bad.robot.http;

import bad.robot.http.observer.Proxy;
import bad.robot.http.observer.ProxyObserver;
import com.google.code.tempusfugit.temporal.Condition;

import java.util.concurrent.TimeoutException;

import static bad.robot.http.HttpClients.anApacheClient;
import static com.google.code.tempusfugit.temporal.Duration.millis;
import static com.google.code.tempusfugit.temporal.Duration.seconds;
import static com.google.code.tempusfugit.temporal.Timeout.timeout;
import static com.google.code.tempusfugit.temporal.WaitFor.waitOrTimeout;

public class Main {

    public static void main(String... args) throws TimeoutException, InterruptedException {
        Proxy proxy = new Proxy();
        proxy.start();
        final HttpResponse response;
        try {
            response = anApacheClient().with(new ProxyObserver()).with(millis(200)).get(new Url("http://www.google.com:8080"));
            waitOrTimeout(new Condition() {
                @Override
                public boolean isSatisfied() {
                    return response.ok();
                }
            }, timeout(seconds(2)));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            proxy.stop();
        }
    }

}
