/*
 * Copyright (c) 2011-2019, simple-http committers
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

package simplehttp.configuration;

import java.net.URL;
import java.util.Optional;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static simplehttp.Url.url;

public class SystemPropertyProxy extends Proxy {

    private static final String proxyUrl = "http.proxyHost";
    private static final String port = "http.proxyPort";
    private static final String user = "http.proxyUser";
    private static final String password = "http.proxyPassword";

    public static Optional<Proxy> systemPropertyProxy() {
        try {
            return Optional.of(new SystemPropertyProxy(getUrlFromSystemProperties()));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    private SystemPropertyProxy(URL value) {
        super(value);
    }

    public static URL getUrlFromSystemProperties() {
        String url = (String) System.getProperties().get(SystemPropertyProxy.proxyUrl);
        String port = (String) System.getProperties().get(SystemPropertyProxy.port);
        if (isEmpty(url) || isEmpty(port))
            throw new IllegalArgumentException(format("'%s' and/or '%s' were not specified as system properties, use -D or set programatically", SystemPropertyProxy.proxyUrl, SystemPropertyProxy.port));
        if (url.toLowerCase().contains("://"))
            throw new IllegalArgumentException(format("no need to set a protocol on the proxy URL specified by the system property '%s'", proxyUrl));
        String username = (String) System.getProperties().get(SystemPropertyProxy.user);
        String password = (String) System.getProperties().get(SystemPropertyProxy.password);
        if (isEmpty(username) || isEmpty(password))
            return url(format("http://%s:%s", url, port));
        return url(format("http://%s:%s@%s:%s", username, password, url, port));
    }
}
