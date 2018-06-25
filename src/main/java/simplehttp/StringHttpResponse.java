/*
 * Copyright (c) 2011-2018, simple-http committers
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

import static java.lang.String.format;

public class StringHttpResponse implements HttpResponse {

    private final int statusCode;
    private final String statusMessage;
    private final String content;
    private final Headers headers;
    private final String originatingUri;

    public StringHttpResponse(int statusCode, String statusMessage, String content, Headers headers, String originatingUri) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.content = content;
        this.headers = headers;
        this.originatingUri = originatingUri;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String getStatusMessage() {
        return statusMessage;
    }

    @Override
    public StringMessageContent getContent() {
        return new StringMessageContent(content);
    }

    @Override
    public Headers getHeaders() {
        return headers;
    }

    @Override
    public String getOriginatingUri() {
        return originatingUri;
    }

    @Override
    public boolean ok() {
        return statusCode == 200;
    }

    @Override
    public String toString() {
        return format("%s{statusCode=%d, statusMessage='%s', content='%s', headers='%s', originatingUri='%s'}", this.getClass().getSimpleName(), statusCode, statusMessage, content, headers, originatingUri);
    }
}
