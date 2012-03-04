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

package bad.robot.http.listener;

import bad.robot.http.*;
import org.apache.log4j.Logger;

import java.net.URL;

public class LoggingHttpClient implements HttpClient {

    private final HttpClient delegate;
    private final Logger log;

    public LoggingHttpClient(HttpClient delegate, Logger logger) {
        this.log = logger;
        this.delegate = delegate;
    }

    @Override
    public HttpResponse get(final URL url) throws HttpException {
        try {
            return delegate.get(url);
        } finally {
            if (log.isInfoEnabled())
                log.info(request(url, new HttpGetMessage()));
        }
    }

    @Override
    public HttpResponse get(URL url, Headers headers) throws HttpException {
        try {
            return delegate.get(url, headers);
        } finally {
            if (log.isInfoEnabled())
                log.info(request(url, new HttpGetMessage(headers)));
        }
    }

    @Override
    public HttpResponse post(URL url, HttpPost message) throws HttpException {
        try {
            return delegate.post(url, message);
        } finally {
            if (log.isInfoEnabled())
                log.info(request(url, message));
        }
    }

    @Override
    public HttpResponse put(URL url, HttpPut message) throws HttpException {
        try {            
            return delegate.put(url, message);
        } finally {
            if (log.isInfoEnabled())
                log.info(request(url, message));
        }
    }

    @Override
    public HttpResponse delete(URL url) throws HttpException {
        try {
            return delegate.delete(url);
        } finally {
            if (log.isInfoEnabled())
                log.info(request(url, new HttpDeleteMessage()));
        }
    }

    @Override
    public void shutdown() {
        delegate.shutdown();
    }

    private String request(final URL url, final HttpRequest message) {
        RawHttpRequest rawRequest = new RawHttpRequest(url);
        message.accept(rawRequest);
        return rawRequest.asString();
    }

    private static class RawHttpRequest implements HttpRequestVisitor {

        private final URL url;
        private final StringBuilder builder;

        public RawHttpRequest(URL url) {
            this.url = url;
            this.builder = new StringBuilder();
        }

        @Override
        public void visit(HttpGet message) {
            asHttpString(message, "GET");
        }

        @Override
        public void visit(HttpDelete message) {
            asHttpString(message, "DELETE");
        }

        @Override
        public void visit(FormUrlEncodedMessage message) {
            asHttpString(message, "POST");
        }

        @Override
        public void visit(UnencodedStringMessage message) {
            asHttpString(message, "POST");
        }

        private void asHttpString(HttpMessage message, String method) {
            builder.append(method).append(" ").append(url.toExternalForm()).append(" HTTP/1.1").append("\n");
            for (Header header : message.getHeaders())
                builder.append(header.name()).append(": ").append(header.value()).append("\n");
            builder.append("\n").append(message.getContent().asString());
        }

        public String asString() {
            return builder.toString();
        }
    }
}
