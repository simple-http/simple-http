package bad.robot.http.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.net.URL;

public class UrlMatcher extends TypeSafeMatcher<URL> {

    private final String path;

    @Factory
    public static Matcher<URL> containsPath(String path) {
        return new UrlMatcher(path);
    }

    public UrlMatcher(String path) {
        this.path = path;
    }

    @Override
    public boolean matchesSafely(URL url) {
        return getFullPathFrom(url).contains(path);
    }

    private static String getFullPathFrom(URL url) {
        StringBuilder builder = new StringBuilder(url.getProtocol()).append("://").append(url.getHost());
        if (portExists(url))
            builder.append(":").append(url.getPort());
        builder.append(url.getPath());
        if (queryExists(url))
            builder.append("?").append(url.getQuery()).toString();
        return builder.toString();
    }

    private static boolean queryExists(URL url) {
        return url.getQuery() != null;
    }

    private static boolean portExists(URL url) {
        return url.getPort() != -1;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("with a URL containing the text ").appendValue(path);
    }
}