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

package bad.robot.http.apache;

import bad.robot.http.*;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.Map;

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
    public void visit(FormUrlEncodedMessage formUrlEncodedMessage) {
        try {
            FormParameters content = formUrlEncodedMessage.getContent();
            entity = new UrlEncodedFormEntity(content.transform(asApacheNameValuePair()));
        } catch (UnsupportedEncodingException e) {
            throw new HttpException(e);
        }
    }

    @Override
    public void visit(UnencodedStringMessage unencodedStringMessage) {
        try {
            StringMessageContent content = unencodedStringMessage.getContent();
            entity = new StringEntity(content.asString());
        } catch (UnsupportedEncodingException e) {
            throw new HttpException(e);
        }        
    }

    private Transform<Map.Entry<String, String>, NameValuePair> asApacheNameValuePair() {
        return new Transform<Map.Entry<String, String>, NameValuePair>() {
            @Override
            public NameValuePair call(Map.Entry<String, String> tuple) {
                return new BasicNameValuePair(tuple.getKey(), tuple.getValue());
            }
        };
    }
}
