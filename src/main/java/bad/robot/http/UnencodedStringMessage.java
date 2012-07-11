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

package bad.robot.http;

import static bad.robot.http.CharacterSet.defaultCharacterSet;
import static bad.robot.http.SimpleHeaders.noHeaders;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

public class UnencodedStringMessage implements HttpPut, HttpPost {

    private final String content;
    private final Headers headers;
    private final CharacterSet characterSet;

    public UnencodedStringMessage(String content) {
        this(content, noHeaders());
    }

    public UnencodedStringMessage(String content, Headers headers) {
        this(content, headers, defaultCharacterSet);
    }

    public UnencodedStringMessage(String content, CharacterSet characterSet) {
        this(content, noHeaders(), characterSet);
    }

    public UnencodedStringMessage(String content, Headers headers, CharacterSet characterSet) {
        this.content = content;
        this.headers = headers;
        this.characterSet = characterSet;
    }

    @Override
    public StringMessageContent getContent() {
        return new StringMessageContent(content);
    }

    @Override
    public Headers getHeaders() {
        return headers;
    }

    public String characterSet() {
        return characterSet.asString();
    }

    @Override
    public void accept(HttpRequestVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object that) {
        return reflectionEquals(this, that);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "content='" + content + "\', " +
                "headers='" + headers + "\'" +
                '}';
    }
}
