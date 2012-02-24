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

import bad.robot.http.Header;
import bad.robot.http.Headers;
import bad.robot.http.HttpResponse;
import bad.robot.http.matchers.apache.ApacheHeaderMatcher;
import bad.robot.http.matchers.apache.ApacheHttpUriRequestContentMatcher;
import bad.robot.http.matchers.apache.ApacheHttpUriRequestHeaderMatcher;
import bad.robot.http.matchers.apache.ApacheHttpUriRequestUrlMatcher;
import org.apache.http.client.methods.HttpUriRequest;
import org.hamcrest.Matcher;

import java.net.URL;

import static org.hamcrest.Matchers.equalTo;

public class Matchers {

    public static Matcher<HttpResponse> hasStatus(int status) {
        return HttpResponseStatusCodeMatcher.hasStatus(status);
    }

    public static Matcher<HttpResponse> hasStatusMessage(String message) {
        return HttpResponseStatusMessageMatcher.hasStatusMessage(message);
    }

    public static Matcher<HttpResponse> hasContent(String content) {
        return HttpResponseMessageContentStringMatcher.hasContent(equalTo(content));
    }

    public static Matcher<HttpResponse> hasContent(Matcher<String> content) {
        return HttpResponseMessageContentStringMatcher.hasContent(content);
    }

    public static Matcher<HttpResponse> hasHeader(Header header) {
        return HttpResponseHeaderMatcher.hasHeader(header);
    }

    public static Matcher<HttpResponse> hasHeader(String name, Matcher<String> value) {
        return HttpResponseHeaderStringMatcher.hasHeaderWithValue(name, value);
    }

    public static Matcher<URL> containsPath(String path) {
        return UrlMatcher.containsPath(path);
    }

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
