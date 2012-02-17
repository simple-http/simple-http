package bad.robot.http.matchers.apache;

import org.apache.http.Header;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class ApacheHeaderMatcher extends TypeSafeMatcher<Header> {

    private final String name;
    private final String value;

    @Factory
    public static Matcher<Header> apacheHeader(String name, String value) {
        return new ApacheHeaderMatcher(name, value);
    }

    public ApacheHeaderMatcher(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public boolean matchesSafely(Header header) {
        return header.getName().equals(name) && header.getValue().equals(value);
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(name).appendValue(value);
    }
}
