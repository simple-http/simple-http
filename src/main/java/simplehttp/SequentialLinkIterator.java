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

package simplehttp;

import java.net.URL;
import java.util.Iterator;

import static java.lang.String.format;

public class SequentialLinkIterator implements Iterator<HttpResponse>, Iterable<HttpResponse> {

    private final HttpClient http;
    private final Headers headers;
    private Link link;

    public static SequentialLinkIterator sequentialLinkIterator(HttpResponse response, HttpClient http) {
        return new SequentialLinkIterator(response.getHeaders(), http, EmptyHeaders.emptyHeaders());
    }

    public static SequentialLinkIterator sequentialLinkIterator(HttpResponse response, HttpClient http, Headers headers) {
        return new SequentialLinkIterator(response.getHeaders(), http, headers);
    }

    private SequentialLinkIterator(Headers headers, HttpClient http, Headers prototypeHeaders) {
        this.http = http;
        this.headers = prototypeHeaders;
        try {
            link = new Link(headers);
        } catch (MalformedUrlRuntimeException e) {
            throw new HttpException("Link header was not valid when trying to construct a sequence of relative links");
        }
    }

    @Override
    public Iterator<HttpResponse> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return link.next() != null;
    }

    @Override
    public HttpResponse next() {
        URL next = link.next();
        HttpResponse response = http.get(next, headers);
        try {
            link = new Link(response.getHeaders());
        } catch (MalformedUrlRuntimeException e) {
            throw new HttpException(format("Malformed Url in the 'link' header of the response to %s", next));
        }
        return response;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
