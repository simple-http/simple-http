package bad.robot.http;

import java.net.URL;
import java.util.Iterator;

import static java.lang.String.format;

public class SequentialLinkIterator implements Iterator<HttpResponse>, Iterable<HttpResponse> {

    private final HttpClient http;
    private final Headers headers;
    private Link link;

    public static SequentialLinkIterator sequentialLinkIterator(HttpResponse response, HttpClient http) {
        return new SequentialLinkIterator(response.getHeaders(), http, EmptyHeaders.emptyHeaders());
    }

    public static SequentialLinkIterator sequentialLinkIterator(HttpResponse response, HttpClient http, Headers headers) {
        return new SequentialLinkIterator(response.getHeaders(), http, headers);
    }

    private SequentialLinkIterator(Headers headers, HttpClient http, Headers prototypeHeaders) {
        this.http = http;
        this.headers = prototypeHeaders;
        try {
            link = new Link(headers);
        } catch (MalformedUrlRuntimeException e) {
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
        HttpResponse response = http.get(next, headers);
        try {
            link = new Link(response.getHeaders());
        } catch (MalformedUrlRuntimeException e) {
            throw new HttpException(format("Malformed Url in the 'link' header of the response to %s", next));
        }
        return response;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
