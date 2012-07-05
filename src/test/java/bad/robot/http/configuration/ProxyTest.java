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

package bad.robot.http.configuration;

import org.apache.http.HttpHost;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class ProxyTest {

    private final Mockery context = new JUnit4Mockery();
    private final Configurable configurable = context.mock(Configurable.class);

    @Test
    public void configures() throws MalformedURLException {
        final URL url = new URL("http://baddotrobot.com");
        Proxy proxy = Proxy.proxy(url);
        context.checking(new Expectations() {{
            oneOf(configurable).setTo(new HttpHost(url.getHost(), url.getPort(), url.getProtocol()));
        }});
        proxy.applyTo(configurable);
    }
}
