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

package bad.robot.http.matchers;

import bad.robot.http.*;
import bad.robot.http.matchers.apache.ApacheHeaderMatcher;
import bad.robot.http.matchers.apache.ApacheHttpUriRequestContentMatcher;
import bad.robot.http.matchers.apache.ApacheHttpUriRequestHeaderMatcher;
import bad.robot.http.matchers.apache.ApacheHttpUriRequestUrlMatcher;
import org.apache.http.client.methods.HttpUriRequest;
import org.hamcrest.Matcher;

import java.net.URL;
import java.nio.charset.Charset;

/**
 * This is your first port of call if you're looking for helpful HTTP related {@link Matcher}s.
 */
public class Matchers {

    public static <T> Matcher<T> has(Matcher<T> matcher) {
        return matcher;
    }

    public static Matcher<HttpPost> post(Matcher<? extends HttpMessage> matcher) {
        return (Matcher<HttpPost>) matcher;
    }

    public static Matcher<HttpPut> put(Matcher<? extends HttpMessage> matcher) {
        return (Matcher<HttpPut>) matcher;
    }

    public static <T extends HttpMessage> Matcher<T> content(String content) {
        return HttpMessageContentStringMatcher.<T>content(content);
    }

    public static <T extends HttpMessage> Matcher<T> content(Matcher<String> mather) {
        return HttpMessageContentStringMatcher.<T>content(mather);
    }

    public static <T extends HttpMessage> Matcher<T> binaryContent(Matcher<byte[]> matcher) {
        return HttpMessageContentByteArrayMatcher.<T>content(matcher, Charset.defaultCharset().name());
    }

    public static <T extends HttpMessage> Matcher<T> binaryContent(byte[] content) {
        return HttpMessageContentByteArrayMatcher.<T>content(content, Charset.defaultCharset().name());
    }

    public static <T extends HttpMessage> Matcher<T> binaryContent(Matcher<byte[]> matcher, String characterSet) {
        return HttpMessageContentByteArrayMatcher.<T>content(matcher, characterSet);
    }

    public static <T extends HttpMessage> Matcher<T> has(Header header) {
        return HttpMessageHeaderMatcher.<T>has(header);
    }

    public static <T extends HttpMessage> Matcher<T> headerWithValue(String name, Matcher<String> value) {
        return HttpMessageHeaderValueMatcher.<T>headerWithValue(name, value);
    }

    public static Matcher<Headers> hasHeader(Header header) {
        return HeadersMatcher.hasHeader(header);
    }

    public static Matcher<Headers> hasHeaders(Header... headers) {
        return HeadersMatcher.hasHeaders(headers);
    }

    public static Matcher<Headers> has(Header... headers) {
        return HeadersMatcher.hasHeaders(headers);
    }

    public static Matcher<HttpResponse> status(int status) {
        return HttpResponseStatusCodeMatcher.status(status);
    }

    public static Matcher<HttpResponse> statusMessage(String message) {
        return HttpResponseStatusMessageMatcher.statusMessage(message);
    }

    public static Matcher<HttpResponse> statusMessage(Matcher<String> matcher) {
        return HttpResponseStatusMessageMatcher.statusMessage(matcher);
    }

    public static Matcher<URL> containsPath(String path) {
        return UrlMatcher.containsPath(path);
    }

    // TODO add matcher for Apache headers (plural)

    public static Matcher<org.apache.http.Header> apacheHeader(String name, String value) {
        return ApacheHeaderMatcher.apacheHeader(name, value);
    }

    public static Matcher<HttpUriRequest> requestContaining(Headers headers) {
        return ApacheHttpUriRequestHeaderMatcher.requestContaining(headers);
    }

    public static Matcher<HttpUriRequest> requestWith(URL url) {
        return ApacheHttpUriRequestUrlMatcher.requestWith(url);
    }

    public static Matcher<? extends HttpUriRequest> messageContaining(String content) {
        return ApacheHttpUriRequestContentMatcher.messageContaining(content);
    }
}
