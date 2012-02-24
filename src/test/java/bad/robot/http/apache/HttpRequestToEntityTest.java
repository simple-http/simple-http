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

import bad.robot.http.FormParameters;
import bad.robot.http.FormUrlEncodedMessage;
import bad.robot.http.UnencodedStringMessage;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.junit.Test;

import java.io.IOException;

import static bad.robot.http.FormParameters.params;
import static bad.robot.http.matchers.Matchers.apacheHeader;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class HttpRequestToEntityTest {

    private static final String encodedContent = "name=I%27m+a+cheese+sandwich";

    @Test
    public void shouldConvertEncodedContent() throws IOException {
        FormParameters tuples = params("name", "I'm a cheese sandwich");
        HttpRequestToEntity converter = new HttpRequestToEntity(new FormUrlEncodedMessage(tuples));
        HttpEntity entity = converter.asHttpEntity();

        String content = IOUtils.toString(entity.getContent());
        assertThat(content, is(encodedContent));
        assertThat(entity.getContentType(), is(apacheHeader("Content-Type", "application/x-www-form-urlencoded; charset=ISO-8859-1")));
        assertThat(entity.getContentLength(), is((long) encodedContent.getBytes().length));
    }

    @Test
    public void shouldIgnoreDuplicateKeysInEncodedContent() throws IOException {
        FormParameters tuples = params("name", "value", "name", "anotherValue");
        HttpRequestToEntity converter = new HttpRequestToEntity(new FormUrlEncodedMessage(tuples));
        HttpEntity entity = converter.asHttpEntity();

        String content = IOUtils.toString(entity.getContent());
        assertThat(content, is("name=anotherValue"));
    }
    
    @Test
    public void shouldConvertStringContent() throws IOException {
        HttpRequestToEntity converter = new HttpRequestToEntity(new UnencodedStringMessage("nom nom nom"));
        HttpEntity entity = converter.asHttpEntity();

        String content = IOUtils.toString(entity.getContent());
        assertThat(content, is("nom nom nom"));
        assertThat(entity.getContentType(), is(apacheHeader("Content-Type", "text/plain; charset=ISO-8859-1")));
        assertThat(entity.getContentLength(), is((long) "nom nom nom".getBytes().length));
    }

}
