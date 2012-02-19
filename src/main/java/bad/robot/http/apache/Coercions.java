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

import bad.robot.http.Header;
import bad.robot.http.Headers;
import jedi.functional.Functor;
import org.apache.http.message.BasicHeader;

import java.util.List;

import static bad.robot.http.SimpleHeader.header;
import static bad.robot.http.SimpleHeaders.headers;
import static jedi.functional.FunctionalPrimitives.collect;

public class Coercions {

    public static org.apache.http.Header[] asApacheBasicHeader(Headers headers) {
        return collect(headers, new Functor<Header, org.apache.http.Header>() {
            @Override
            public org.apache.http.Header execute(Header header) {
                return new BasicHeader(header.name(), header.value());
            }
        }).toArray(new org.apache.http.Header[]{});
    }
    
    public static Headers asHeaders(org.apache.http.Header[] headers) {
        List<Header> list = collect(headers, new Functor<org.apache.http.Header, Header>() {
            @Override
            public Header execute(org.apache.http.Header header) {
                return header(header.getName(), header.getValue());
            }
        });
        return headers(list.toArray(new Header[list.size()]));
    }
}
