package bad.robot.http;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static bad.robot.http.HeaderList.headers;
import static bad.robot.http.HeaderPair.header;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class LinkTest {

    private final Headers example = headers(header("link",
            "<http://example.com/customers?page=4&per_page=50>; rel=\"prev\"," +
            "<http://example.com/customers?page=6&per_page=50>; rel=\"next\"," +
            "<http://example.com/customers?page=1&per_page=50>; rel=\"first\"," +
            "<http://example.com/customers?page=10&per_page=50>; rel=\"last\""));

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
        Link link = new Link(headers(header("link", "<http://example.com/customers?page=4&per_page=50>; rel=\"previous\"")));
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
    public void caseInsensitive() throws MalformedURLException {
        Link link = new Link(headers(header("link", "<http://example.com/customers>; rel=\"NEXT\"")));
        assertThat(link.next(), is(new URL("http://example.com/customers")));
    }

    @Test
    public void noLinkHeader() throws MalformedURLException {
        Link link = new Link(headers(header("accept", "application/json")));
        assertThat(link.first(), is(nullValue()));
        assertThat(link.previous(), is(nullValue()));
        assertThat(link.next(), is(nullValue()));
        assertThat(link.last(), is(nullValue()));
    }

    // TODO
    @Test (expected = StringIndexOutOfBoundsException.class)
    public void badHeader() throws MalformedURLException {
        new Link(headers(header("link", "asdas")));
    }

    @Test (expected = MalformedURLException.class)
    public void badUrlAsPerJavasImplmentation() throws MalformedURLException {
        new Link(headers(header("link", "<classpath://example>; rel=\"next\"")));
    }

    @Test
    public void onlySingleRelativeLink() throws MalformedURLException {
        Link link = new Link(headers(header("link", "<file://example>; rel=\"next\"")));
        assertThat(link.first(), is(nullValue()));
        assertThat(link.previous(), is(nullValue()));
        assertThat(link.next(), is(new URL("file://example")));
        assertThat(link.last(), is(nullValue()));
    }

}
