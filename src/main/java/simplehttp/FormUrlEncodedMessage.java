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

import java.util.Objects;

import static java.lang.String.format;
import static simplehttp.CharacterSet.defaultCharacterSet;
import static simplehttp.EmptyHeaders.emptyHeaders;

public class FormUrlEncodedMessage implements HttpPost {

    private final Headers headers;
    private final FormParameters content;
    private final CharacterSet characterSet;

    public FormUrlEncodedMessage(FormParameters content) {
        this(content, emptyHeaders());
    }

    public FormUrlEncodedMessage(FormParameters content, Headers headers) {
        this(content, headers, defaultCharacterSet);
    }

    public FormUrlEncodedMessage(FormParameters content, CharacterSet characterSet) {
        this(content, emptyHeaders(), characterSet);
    }

    public FormUrlEncodedMessage(FormParameters content, Headers headers, CharacterSet characterSet) {
        this.headers = headers;
        this.content = content;
        this.characterSet = characterSet;
    }

    @Override
    public Headers getHeaders() {
        // Content-Type and Content-Length are already set in UrlParameters (at least for Apache)
        return headers;
    }

    @Override
    public FormParameters getContent() {
        return content;
    }

    public String characterSet() {
        return characterSet.asString();
    }

    @Override
    public void accept(HttpRequestVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FormUrlEncodedMessage that = (FormUrlEncodedMessage) o;
        return Objects.equals(headers, that.headers) &&
            Objects.equals(content, that.content) &&
            characterSet == that.characterSet;
    }

    @Override
    public int hashCode() {
        return Objects.hash(headers, content, characterSet);
    }

    @Override
    public String toString() {
        return format("%s{content='%s', headers='%s'characterSet='%s'}", this.getClass().getSimpleName(), content, headers, characterSet());
    }
}
