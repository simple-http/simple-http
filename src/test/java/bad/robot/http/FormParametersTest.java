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

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static bad.robot.http.FormParameters.params;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class FormParametersTest {
    
    @Test (expected = IllegalArgumentException.class)
    public void shouldAllowOnlyPairs() {
        params("name");
    }

    @Test
    public void shouldAllowNoArguments() {
        params();
    }

    @Test
    public void shouldAllowRetrievalViaTransformation() {
        FormParameters tuples = params("name", "value", "cheese", "ham");
        List<String> values = tuples.transform(asSimpleString());
        assertThat(values.get(0), is("name=value"));
        assertThat(values.get(1), is("cheese=ham"));
    }

    private static Transform<Map.Entry<String, String>, String> asSimpleString() {
        return new Transform<Map.Entry<String, String>, String>() {
            @Override
            public String call(Map.Entry<String, String> tuple) {
                return tuple.getKey() + "=" + tuple.getValue();
            }
        };
    }

}
