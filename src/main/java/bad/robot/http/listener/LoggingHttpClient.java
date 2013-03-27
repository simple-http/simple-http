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

    private static final String lineSeparator = System.getProperty("line.separator");

    private final HttpClient delegate;
    private final Logger log;

    public LoggingHttpClient(HttpClient delegate, Logger logger) {
        this.log = logger;
        this.delegate = delegate;
    }

    @Override
    public HttpResponse get(final URL url) throws HttpException {
        HttpResponse response = null;
        try {
            response = delegate.get(url);
            return response;
        } finally {
            if (log.isInfoEnabled())
                log.info(message(url, new HttpGetMessage(), response));
        }
    }

    @Override
    public HttpResponse get(URL url, Headers headers) throws HttpException {
        HttpResponse response = null;
        try {
            response = delegate.get(url, headers);
            return response;
        } finally {
            if (log.isInfoEnabled())
                log.info(message(url, new HttpGetMessage(headers), response));
        }
    }

    @Override
    public HttpResponse post(URL url, HttpPost message) throws HttpException {
        HttpResponse response = null;
        try {
            response = delegate.post(url, message);
            return response;
        } finally {
            if (log.isInfoEnabled())
                log.info(message(url, message, response));
        }
    }

    @Override
    public HttpResponse put(URL url, HttpPut message) throws HttpException {
        HttpResponse response = null;
        try {
            response = delegate.put(url, message);
            return response;
        } finally {
            if (log.isInfoEnabled())
                log.info(message(url, message, response));
        }
    }

    @Override
    public HttpResponse delete(URL url) throws HttpException {
        HttpResponse response = null;
        try {
            response = delegate.delete(url);
            return response;
        } finally {
            if (log.isInfoEnabled())
                log.info(message(url, new HttpDeleteMessage(), response));
        }
    }

    @Override
    public HttpResponse options(URL url) throws HttpException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void shutdown() {
        delegate.shutdown();
    }

    private String message(URL url, HttpRequest request, HttpResponse response) {
        StringBuilder builder = new StringBuilder().append(request(url, request)).append(lineSeparator);
        if (response != null)
            builder.append(response(response));
        return builder.toString();
    }

    private String request(URL url, final HttpRequest message) {
        RawHttpRequest rawRequest = new RawHttpRequest(url);
        message.accept(rawRequest);
        return rawRequest.asString();
    }
    
    private String response(HttpResponse response) {
        StringBuilder builder = new StringBuilder();
        builder.append("HTTP/1.1 ").append(response.getStatusCode()).append(" ").append(response.getStatusMessage()).append(lineSeparator);
        for (Header header : response.getHeaders())
            builder.append(header.name()).append(": ").append(header.value()).append(lineSeparator);
        builder.append(lineSeparator).append(response.getContent().asString());
        return builder.toString();
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
            builder.append(method).append(" ").append(url.toExternalForm()).append(" HTTP/1.1").append(lineSeparator);
            for (Header header : message.getHeaders())
                builder.append(header.name()).append(": ").append(header.value()).append(lineSeparator);
            builder.append(lineSeparator).append(message.getContent().asString());
        }

        public String asString() {
            return builder.toString();
        }
    }
}
