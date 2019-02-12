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

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import simplehttp.FormUrlEncodedMessage;
import simplehttp.Multipart;
import simplehttp.UnencodedStringMessage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static simplehttp.CharacterSet.UTF_8;
import static simplehttp.FormParameters.params;
import static simplehttp.matchers.Matchers.apacheHeader;

public class HttpRequestToEntityTest {

    @Rule public final ExpectedException exception = ExpectedException.none();
    
    private static final String encodedContent = "name=I%27m+a+cheese+sandwich";

    @Test
    public void shouldConvertEncodedContent() throws IOException {
        HttpRequestToEntity converter = new HttpRequestToEntity(new FormUrlEncodedMessage(params("name", "I'm a cheese sandwich")));
        HttpEntity entity = converter.asHttpEntity();

        String content = IOUtils.toString(entity.getContent());
        assertThat(content, is(encodedContent));
        assertThat(entity.getContentType(), is(apacheHeader("Content-Type", "application/x-www-form-urlencoded; charset=ISO-8859-1")));
        assertThat(entity.getContentLength(), is((long) encodedContent.getBytes().length));
    }

    @Test
    public void shouldAllowAlternateEncoding() {
        HttpRequestToEntity converter = new HttpRequestToEntity(new FormUrlEncodedMessage(params("name", "bob"), UTF_8));
        HttpEntity entity = converter.asHttpEntity();

        assertThat(entity.getContentType(), is(apacheHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")));
    }

    @Test
    public void shouldIgnoreDuplicateKeysInEncodedContent() throws IOException {
        HttpRequestToEntity converter = new HttpRequestToEntity(new FormUrlEncodedMessage(params("name", "value", "name", "anotherValue")));
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

    @Test
    public void shouldEncodeMultipartBodyForFile() throws IOException {
        File file = new File("src/test/resource/example-image.png");
        HttpRequestToEntity converter = new HttpRequestToEntity(new Multipart("upload", file));
        HttpEntity entity = converter.asHttpEntity();

        assertThat(entity.getContentType(), is(apacheHeader("Content-Type", containsString("multipart/form-data;"))));
        String content = IOUtils.toString(entity.getContent());
        assertThat(content, allOf(
            containsString("Content-Disposition: form-data; name=\"upload\"; filename=\"example-image.png\""),
            containsString("Content-Type: application/octet-stream"),
            containsString("PNG")
        ));
    }
    
    @Test
    public void multipartWhenFileDoesntExist() throws IOException {
        File file = new File("no-file.exe");
        HttpRequestToEntity converter = new HttpRequestToEntity(new Multipart("upload", file));
        HttpEntity entity = converter.asHttpEntity();

        exception.expect(FileNotFoundException.class);
        exception.expectMessage("no-file.exe (No such file or directory)");
        entity.getContent();
    }
}
