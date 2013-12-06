package bad.robot.http;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import static java.lang.String.format;

public class SequentialLinkIterator implements Iterator<HttpResponse>, Iterable<HttpResponse> {

    private final HttpClient http;
    private Link link;

    public static SequentialLinkIterator sequentialLinkIterator(HttpResponse response, HttpClient http) {
        return new SequentialLinkIterator(response.getHeaders(), http);
    }

    public static SequentialLinkIterator sequentialLinkIterator(Header header, HttpClient http) {
        return new SequentialLinkIterator(HeaderList.headers(header), http);
    }

    private SequentialLinkIterator(Headers headers, HttpClient http) {
        this.http = http;
        try {
            link = new Link(headers);
        } catch (MalformedURLException e) {
            throw new HttpException("Link header was not valid when trying to construct a sequence of relative links");
        }
    }

    @Override
    public Iterator<HttpResponse> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return link.next() != null;
    }

    @Override
    public HttpResponse next() {
        URL next = link.next();
        HttpResponse response = http.get(next);
        try {
            link = new Link(response.getHeaders());
        } catch (MalformedURLException e) {
            throw new HttpException(format("Malformed Url in the 'link' header of the response to %s", next));
        }
        return response;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
