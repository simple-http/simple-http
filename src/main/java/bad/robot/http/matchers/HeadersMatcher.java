package bad.robot.http.matchers;

import bad.robot.http.Header;
import bad.robot.http.Headers;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.Arrays;
import java.util.List;

public class HeadersMatcher extends TypeSafeMatcher<Headers> {

    private final List<Header> expected;

    private HeadersMatcher(Header[] expected) {
        this.expected = Arrays.asList(expected);
    }

    public static HeadersMatcher headers(Header... headers) {
        return new HeadersMatcher(headers);
    }

    @Override
    public boolean matchesSafely(Headers actual) {
        for (Header header : actual) 
            if (!expected.contains(header))
                return false;
        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expected);
    }
}
