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
import org.apache.http.message.BasicHeader;
import simplehttp.Header;
import simplehttp.Headers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static simplehttp.HeaderList.headers;
import static simplehttp.HeaderPair.header;

public class Coercions {

    public static org.apache.http.Header[] asApacheBasicHeader(Headers headers) {
        List<org.apache.http.Header> list = new ArrayList<org.apache.http.Header>();
        for (Header header : headers)
            list.add(new BasicHeader(header.name(), header.value()));
        return list.toArray(new org.apache.http.Header[list.size()]);
    }

    public static Headers asHeaders(org.apache.http.Header[] headers) {
        List<Header> list = new ArrayList<Header>();
        for (org.apache.http.Header header : headers)
            list.add(header(header.getName(), header.getValue()));
        return headers(list.toArray(new Header[list.size()]));
    }

    public static HttpHost asHttpHost(URL url) {
        return new HttpHost(url.getHost(), url.getPort(), url.getProtocol());
    }
}
