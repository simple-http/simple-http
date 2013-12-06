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

package bad.robot.http.configuration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static bad.robot.http.Url.url;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SystemPropertyProxyTest {

    private static final String proxyUrl = "http.proxyHost";
    private static final String proxyPort = "http.proxyPort";
    private static final String proxyUser = "http.proxyUser";
    private static final String proxyPassword = "http.proxyPassword";

    private String cachedProxyUrl;
    private String cachedProxyPort;

    @Before
    public void cacheAndSimulateUnsetSettings() {
        cachedProxyUrl = System.getProperty(proxyUrl, "");
        cachedProxyPort = System.getProperty(proxyPort, "");

        System.setProperty(proxyUrl, "");
        System.setProperty(proxyPort, "");
    }

    @After
    public void resetSettings() {
        System.setProperty(proxyUrl, cachedProxyUrl);
        System.setProperty(proxyPort, cachedProxyPort);
    }

    @Test (expected = IllegalArgumentException.class)
    public void minimumRequirements() {
        SystemPropertyProxy.systemPropertyProxy();
    }

    @Test (expected = IllegalArgumentException.class)
    public void noNeedForAProtocol() {
        System.setProperty(proxyUrl, "http://example.com");
        SystemPropertyProxy.systemPropertyProxy();
    }

    @Test
    public void acceptUrlAndPortSystemProperty() {
        System.setProperty(proxyUrl, "example.com");
        System.setProperty(proxyPort, "80");
        Proxy proxy = SystemPropertyProxy.systemPropertyProxy();
        assertThat(proxy.value, is(url("http://example.com:80")));
    }

    @Test
    public void acceptUtlWithUsernameAndPassword() {
        System.setProperty(proxyUrl, "example.com");
        System.setProperty(proxyPort, "80");
        System.setProperty(proxyUser, "username");
        System.setProperty(proxyPassword, "password");
        Proxy proxy = SystemPropertyProxy.systemPropertyProxy();
        assertThat(proxy.value, is(url("http://username:password@example.com:80")));
    }
}
