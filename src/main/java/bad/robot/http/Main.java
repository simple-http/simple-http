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

import bad.robot.http.apache.ApacheHttpAuthenticationCredentials;
import bad.robot.http.observer.Proxy;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.net.URI;
import java.net.URL;
import java.util.concurrent.TimeoutException;

import static bad.robot.http.apache.ApacheHttpClientBuilder.anApacheClientWithShortTimeout;

public class Main {

    public static void main(String... args) throws TimeoutException, InterruptedException {
        Proxy server = new Proxy();
        server.start();
        try {
            System.out.println("hello");
            URL proxy = new URL("http://hk-proxy.ap.hedani.net:8080");
            URL altProxy = new URL("http://ocs-proxy.eu.hedani.net:8080");
            URL littleProxy = new URL("http://localhost:8081");

//            final HttpResponse response = anApacheClient().withProxy(new URL("http://localhost:8081")).with(millis(200)).get(new Url("http://localhost:8080"));

// Works
            HttpClient http = anApacheClientWithShortTimeout()
                    .withProxy(new HttpHost(proxy.getHost(), proxy.getPort(), proxy.getProtocol()))
                    .with(new ApacheHttpAuthenticationCredentials(AuthScope.ANY, new UsernamePasswordCredentials("tweston2", "$uperKungFu69")))
                    .build();


// target server fails to response
//            HttpClient bust = anApacheClientWithShortTimeout().build();

            HttpResponse response = http.execute(new HttpGet(new URI("http://localhost:8999")));
            System.out.println(response);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            server.stop();
        }
    }

}
