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

import org.jmock.Expectations;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static simplehttp.EmptyHeaders.emptyHeaders;
import static simplehttp.HeaderList.headers;
import static simplehttp.HeaderPair.header;
import static simplehttp.SequentialLinkIterator.sequentialLinkIterator;


public class SequentialLinkIteratorTest {

    private final JUnitRuleMockery context = new JUnitRuleMockery();
    private final HttpClient http = context.mock(HttpClient.class);

    private final HttpResponse initialResponse = new StringHttpResponse(200, "OK", "0", headers(header("Link", "<http://example.com/first>; rel=\"next\"")), "");
    private final HttpResponse secondResponse = new StringHttpResponse(201, "OK", "1", headers(header("Link", "<http://example.com/second>; rel=\"next\"")), "");
    private final HttpResponse finalResponse = new StringHttpResponse(202, "OK", "2", emptyHeaders(), "");

    private URL firstUrl;
    private URL secondUrl;

    @Before
    public void setUp() throws Exception {
        firstUrl = Url.url("http://example.com/first");
        secondUrl = Url.url("http://example.com/second");
    }

    @Test
    public void doesNotHaveNext() {
        assertThat(sequentialLinkIterator(finalResponse, http).hasNext(), is(false));
    }

    @Test
    public void shouldFollowLinks() {
        final Sequence order = context.sequence("order");
        context.checking(new Expectations() {{
            oneOf(http).get(with(firstUrl), with(any(Headers.class))); will(returnValue(secondResponse)); inSequence(order);
            oneOf(http).get(with(secondUrl), with(any(Headers.class))); will(returnValue(finalResponse)); inSequence(order);
        }});
        String responses = "0";
        for (HttpResponse response : sequentialLinkIterator(initialResponse, http))
            responses += response.getContent().asString();
        assertThat(responses, is("012"));
    }

    @Test
    public void shouldCopyOverHeaders() {
        final Headers headers = headers(header("accept", "application/json"));
        context.checking(new Expectations() {{
            oneOf(http).get(firstUrl, headers); will(returnValue(finalResponse));
        }});
        for (HttpResponse response : sequentialLinkIterator(initialResponse, http, headers)) {}
    }

}
