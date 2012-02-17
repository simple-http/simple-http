package bad.robot.http.matchers.apache;

import bad.robot.http.Headers;
import org.apache.http.client.methods.HttpUriRequest;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static bad.robot.http.apache.Coercions.asHeaders;
import static org.hamcrest.Matchers.is;

public class ApacheHttpUriRequestHeaderMatcher extends TypeSafeMatcher<HttpUriRequest> {

    private final Headers expected;

    @Factory
    public static Matcher<HttpUriRequest> requestContaining(Headers expected) {
        return new ApacheHttpUriRequestHeaderMatcher(expected);
    }

    public ApacheHttpUriRequestHeaderMatcher(Headers expected) {
        this.expected = expected;
    }

    @Override
    public boolean matchesSafely(HttpUriRequest actual) {
        return is(expected).matches(asHeaders(actual.getAllHeaders()));
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expected);
    }
}
