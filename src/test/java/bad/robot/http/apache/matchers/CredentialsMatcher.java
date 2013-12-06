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

package bad.robot.http.apache.matchers;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.AbstractHttpClient;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.net.URL;

import static org.apache.http.auth.AuthScope.ANY_REALM;
import static org.apache.http.auth.AuthScope.ANY_SCHEME;

public class CredentialsMatcher extends TypeSafeDiagnosingMatcher<HttpClient> {

    private final String username;
    private final String password;
    private final AuthScope scope;

    public static Matcher<HttpClient> credentialsProviderContains(URL url, String username, String password) {
        return new CredentialsMatcher(url, username, password);
    }

    private CredentialsMatcher(URL url, String username, String password) {
        this.username = username;
        this.password = password;
        this.scope = new AuthScope(url.getHost(), url.getPort(), ANY_REALM, ANY_SCHEME);
    }

    @Override
    protected boolean matchesSafely(HttpClient actual, Description mismatch) {
        AbstractHttpClient client = (AbstractHttpClient) actual;
        Credentials credentials = client.getCredentialsProvider().getCredentials(scope);
        if (credentials == null) {
            mismatch.appendText("No credentials found for ").appendValue(scope);
            return false;
        }
        if (!username.equals(credentials.getUserPrincipal().getName())) {
            mismatch.appendText("found username ").appendValue(credentials.getUserPrincipal().getName());
            return false;
        }
        if (!password.equals(credentials.getPassword())) {
            mismatch.appendText("found password ").appendValue(credentials.getPassword());
            return false;
        }
        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("containing ").appendValue(username).appendText(" ").appendValue(password).appendText(" for AuthScope ").appendValue(scope);
    }
}
