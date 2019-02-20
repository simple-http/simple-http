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


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;

public class HeaderList implements Headers {

    private final List<Header> headers;

    private HeaderList(List<Header> headers) {
        this.headers = new ArrayList<>(headers);
    }

    public static Headers headers(Header header, Header... headers) {
        return new HeaderList(Stream.concat(Stream.of(header), Arrays.stream(headers)).collect(Collectors.toList()));
    }

    @Override
    public Iterator<Header> iterator() {
        return headers.iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HeaderList headers1 = (HeaderList) o;
        return Objects.equals(headers, headers1.headers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(headers);
    }

    @Override
    public String toString() {
        return format("%s{headers=%s}", this.getClass().getSimpleName(), headers);
    }

    @Override
    public boolean has(String key) {
        return !get(key).equals(new NoHeader());
    }

    @Override
    public Header get(String key) {
        for (Header header : headers)
            if (header.name().equalsIgnoreCase(key))
                return header;
        return new NoHeader();
    }
}
