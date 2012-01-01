/*
 * Copyright (c) 2009-2011, bad robot (london) ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package bad.robot.http.matchers;

import bad.robot.http.HttpResponse;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeMatcher;

public class HttpResponseStatusMessageMatcher extends TypeSafeMatcher<HttpResponse> {
    private final String expected;

    @Factory
    public static HttpResponseStatusMessageMatcher hasStatusMessage(String expected) {
        return new HttpResponseStatusMessageMatcher(expected);
    }

    private HttpResponseStatusMessageMatcher(String expected) {
        this.expected = expected;
    }

    @Override
    public boolean matchesSafely(HttpResponse actual) {
        return actual.getStatusMessage().equals(expected);
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expected);
    }
}
