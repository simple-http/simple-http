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

package simplehttp.apache;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.client.ResponseHandler;
import simplehttp.Headers;
import simplehttp.HttpResponse;
import simplehttp.StringHttpResponse;

import java.io.IOException;

import static simplehttp.apache.Coercions.asHeaders;

class HttpResponseHandler implements ResponseHandler<HttpResponse> {

    private final String originatingUri;
    private final ContentConsumingStrategy consumer;

    HttpResponseHandler(HttpRequest originatingRequest, ContentConsumingStrategy consumer) {
        this.originatingUri = originatingRequest.getRequestLine().getUri();
        this.consumer = consumer;
    }

    @Override
    public HttpResponse handleResponse(org.apache.http.HttpResponse response) throws IOException {
        return new StringHttpResponse(getStatusCodeFrom(response), getStatusMessageFrom(response), getContentFrom(response), getHeadersFrom(response), originatingUri);
    }

    private static String getStatusMessageFrom(org.apache.http.HttpResponse response) {
        return response.getStatusLine().getReasonPhrase();
    }

    private static int getStatusCodeFrom(org.apache.http.HttpResponse response) {
        return response.getStatusLine().getStatusCode();
    }

    private static Headers getHeadersFrom(org.apache.http.HttpResponse response) {
        return asHeaders(response.getAllHeaders());
    }

    private String getContentFrom(org.apache.http.HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        if (entity == null)
            return "";
        return consumer.toString(entity);
    }

}
