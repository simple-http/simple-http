package bad.robot.http;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LinkTest {

    private final String example =
            "<http://example.com/customers?page=4&per_page=50>; rel=\"prev\"," +
                    "<http://example.com/customers?page=6&per_page=50>; rel=\"next\"," +
                    "<http://example.com/customers?page=1&per_page=50>; rel=\"first\"," +
                    "<http://example.com/customers?page=10&per_page=50>; rel=\"last\"";

    private final Link link;

    public LinkTest() throws MalformedURLException {
        link = new Link(example);
    }

    @Test
    public void nextUri() throws MalformedURLException {
        assertThat(link.next(), is(new URL("http://example.com/customers?page=6&per_page=50")));
    }

    @Test
    public void prevUri() throws MalformedURLException {
        assertThat(link.previous(), is(new URL("http://example.com/customers?page=4&per_page=50")));
    }

    @Test
    public void previousUri() throws MalformedURLException {
        Link link = new Link("<http://example.com/customers?page=4&per_page=50>; rel=\"previous\"");
        assertThat(link.previous(), is(new URL("http://example.com/customers?page=4&per_page=50")));
    }

    @Test
    public void firstUri() throws MalformedURLException {
        assertThat(link.first(), is(new URL("http://example.com/customers?page=1&per_page=50")));
    }

    @Test
    public void lastUri() throws MalformedURLException {
        assertThat(link.last(), is(new URL("http://example.com/customers?page=10&per_page=50")));
    }

    @Test
    public void caseInsensative() throws MalformedURLException {
        Link link = new Link("<http://example.com/customers>; rel=\"NEXT\"");
        assertThat(link.next(), is(new URL("http://example.com/customers")));
    }

}
