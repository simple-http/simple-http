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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

public class FormParameters implements MessageContent {

    private final Map<String, String> parameters = new HashMap<String, String>();

    public static FormParameters params(String... values) {
        return new FormParameters(values);
    }

    private FormParameters(String... values) {
        if (values.length % 2 != 0)
            throw new IllegalArgumentException("should be a even number of arguments, received" + values.length);
        toMap(values);
    }

    private void toMap(String[] values) {
        for (int i = 0; i < values.length; i += 2)
            parameters.put(values[i], values[i + 1]);
    }

    public <T> List<T> transform(Transform<Map.Entry<String, String>, T> transform) {
        List<T> pairs = new ArrayList<T>();
        for (Map.Entry<String, String> parameter : parameters.entrySet())
            pairs.add(transform.call(parameter));
        return pairs;
    }

    @Override
    public String asString() {
        StringBuilder builder = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = parameters.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> parameter = iterator.next();
            builder.append(encode(parameter.getKey())).append("=").append(encode(parameter.getValue()));
            if (iterator.hasNext())
                builder.append("&");
        }
        return builder.toString();
    }

    private String encode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new HttpException(e);
        }
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
        return asString();
    }
}
