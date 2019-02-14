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

package simplehttp.apache;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicNameValuePair;
import simplehttp.*;

import java.io.UnsupportedEncodingException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Map;

import static org.apache.http.entity.ContentType.DEFAULT_BINARY;
import static org.apache.http.entity.mime.HttpMultipartMode.BROWSER_COMPATIBLE;

class HttpRequestToEntity implements HttpRequestVisitor {

    private final HttpRequest message;
    private HttpEntity entity;

    public HttpRequestToEntity(HttpRequest message) {
        this.message = message;
    }

    public HttpEntity asHttpEntity() {
        message.accept(this);
        return entity;
    }

    @Override
    public void visit(HttpDelete message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(HttpGet message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(FormUrlEncodedMessage message) {
        try {
            FormParameters content = message.getContent();
            entity = new UrlEncodedFormEntity(content.transform(asApacheNameValuePair()), message.characterSet());
        } catch (UnsupportedEncodingException e) {
            throw new HttpException(e);
        }
    }

    @Override
    public void visit(UnencodedStringMessage message) {
        try {
            StringMessageContent content = message.getContent();
            entity = new StringEntity(content.asString(), message.characterSet());
        } catch (UnsupportedCharsetException e) {
            throw new HttpException(e);
        }        
    }

    @Override
    public void visit(Multipart multipart) {
		entity = MultipartEntityBuilder
			.create()
			.setMode(BROWSER_COMPATIBLE)
			.addBinaryBody(multipart.getName(), multipart.getFile(), DEFAULT_BINARY, multipart.getFile().getName())
			.build();
    }

    private Transform<Map.Entry<String, String>, NameValuePair> asApacheNameValuePair() {
        return tuple -> new BasicNameValuePair(tuple.getKey(), tuple.getValue());
    }
}
