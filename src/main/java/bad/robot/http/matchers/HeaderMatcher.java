package bad.robot.http.matchers;

import bad.robot.http.Header;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeMatcher;

public class HeaderMatcher extends TypeSafeMatcher<Header> {

    private final Header header;

    @Factory
    public static HeaderMatcher hasHeader(Header expected) {
        return new HeaderMatcher(expected);
    }

    public HeaderMatcher(Header header) {
        this.header = header;
    }

    @Override
    public boolean matchesSafely(Header actual) {
        return header.name().equals(actual.name()) && header.value().equals(actual.value());
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(header);
    }

}
