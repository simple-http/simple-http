package bad.robot.http.matchers.apache;

import org.apache.http.client.methods.HttpUriRequest;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.net.URL;
import java.util.concurrent.Callable;

import static com.google.code.tempusfugit.ExceptionWrapper.wrapAsRuntimeException;

public class ApacheHttpUriRequestUrlMatcher extends TypeSafeMatcher<HttpUriRequest> {

    private final URL url;

    @Factory
    public static Matcher<HttpUriRequest> requestWith(URL url) {
        return new ApacheHttpUriRequestUrlMatcher(url);
    }

    public ApacheHttpUriRequestUrlMatcher(URL url) {
        this.url = url;
    }

    @Override
    public boolean matchesSafely(HttpUriRequest request) {
        return wrapAsRuntimeException(matchUrlTo(request));
    }

    private Callable<Boolean> matchUrlTo(final HttpUriRequest request) {
        return new Callable<Boolean>(){
            @Override
            public Boolean call() throws Exception {
                return url.toExternalForm().equals(request.getURI().toURL().toExternalForm());
            }
        };
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Url contains ").appendValue(url);
    }
}
