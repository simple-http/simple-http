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

import jedi.functional.Functor;

import java.util.*;

import static jedi.functional.FunctionalPrimitives.collect;

public class FormParameters implements MessageContent {

    private final Map<String, String> tuples = new HashMap<String, String>();

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
            tuples.put(values[i], values[i + 1]);
    }

    public <T> List<T> transform(Transform<Map.Entry<String, String>, T> transform) {
        List<T> pairs = new ArrayList<T>();
        for (Map.Entry<String, String> tuple : tuples.entrySet())
            pairs.add(transform.call(tuple));
        return pairs;
    }

    @Override
    public String asString() {
        List<String> list = collect(tuples.entrySet(), new Functor<Map.Entry<String, String>, String>() {
            @Override
            public String execute(Map.Entry<String, String> tuple) {
                return tuple.getKey() + "=" + tuple.getValue();
            }
        });
        return Arrays.toString(list.toArray());
    }
}
