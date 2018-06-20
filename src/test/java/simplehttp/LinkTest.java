/*
 * Copyright (c) 2011-2013, bad robot (london) ltd
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

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static simplehttp.HeaderList.headers;
import static simplehttp.HeaderPair.header;
import static simplehttp.Url.url;

public class LinkTest {

    private final Headers example = headers(header("link",
            "<http://example.com/customers?page=4&per_page=50>; rel=\"prev\"," +
            "<http://example.com/customers?page=6&per_page=50>; rel=\"next\"," +
            "<http://example.com/customers?page=1&per_page=50>; rel=\"first\"," +
            "<http://example.com/customers?page=10&per_page=50>; rel=\"last\""));

    private final Link link = new Link(example);

    @Test
    public void nextUri() {
        assertThat(link.next(), is(url("http://example.com/customers?page=6&per_page=50")));
    }

    @Test
    public void prevUri() {
        assertThat(link.previous(), is(url("http://example.com/customers?page=4&per_page=50")));
    }

    @Test
    public void previousUri() {
        Link link = new Link(headers(header("link", "<http://example.com/customers?page=4&per_page=50>; rel=\"previous\"")));
        assertThat(link.previous(), is(url("http://example.com/customers?page=4&per_page=50")));
    }

    @Test
    public void firstUri() {
        assertThat(link.first(), is(url("http://example.com/customers?page=1&per_page=50")));
    }

    @Test
    public void lastUri() {
        assertThat(link.last(), is(url("http://example.com/customers?page=10&per_page=50")));
    }

    @Test
    public void caseInsensitive() {
        Link link = new Link(headers(header("link", "<http://example.com/customers>; rel=\"NEXT\"")));
        assertThat(link.next(), is(url("http://example.com/customers")));
    }

    @Test
    public void noLinkHeader() {
        Link link = new Link(headers(header("accept", "application/json")));
        assertThat(link.first(), is(nullValue()));
        assertThat(link.previous(), is(nullValue()));
        assertThat(link.next(), is(nullValue()));
        assertThat(link.last(), is(nullValue()));
    }

    // TODO
    @Test (expected = StringIndexOutOfBoundsException.class)
    public void badHeader() {
        new Link(headers(header("link", "asdas")));
    }

    @Test (expected = MalformedUrlRuntimeException.class)
    public void badUrlAsPerJavasImplmentation() {
        new Link(headers(header("link", "<classpath://example>; rel=\"next\"")));
    }

    @Test
    public void onlySingleRelativeLink() {
        Link link = new Link(headers(header("link", "<file://example>; rel=\"next\"")));
        assertThat(link.first(), is(nullValue()));
        assertThat(link.previous(), is(nullValue()));
        assertThat(link.next(), is(url("file://example")));
        assertThat(link.last(), is(nullValue()));
    }

    @Test
    public void singleQuotes() {
        Link link = new Link(headers(header("link",
                "<http://example.com?page=2&per_page=25&bank_account=14127&from_date=2013-11-01&to_date=2013-11-30>; rel='next', " +
                "<http://example.com?page=2&per_page=25&bank_account=14127&from_date=2013-11-01&to_date=2013-11-30>; rel='last'\n")));
        assertThat(link.first(), is(nullValue()));
        assertThat(link.previous(), is(nullValue()));
        assertThat(link.next(), is(url("http://example.com?page=2&per_page=25&bank_account=14127&from_date=2013-11-01&to_date=2013-11-30")));
        assertThat(link.last(), is(url("http://example.com?page=2&per_page=25&bank_account=14127&from_date=2013-11-01&to_date=2013-11-30")));

    }

}
