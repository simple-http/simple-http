package bad.robot.http;

import jedi.functional.EmptyIterator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class SimpleHeaders implements Headers {

    private final List<Header> headers;

    private SimpleHeaders(List<Header> pairs) {
        headers = new ArrayList<Header>(pairs);
    }

    public static SimpleHeaders headers(Header... pairs) {
        return new SimpleHeaders(Arrays.asList(pairs));
    }

    @Override
    public Iterator<Header> iterator() {
        return headers.iterator();
    }

    public static Headers noHeaders() {
        return new Headers() {
            @Override
            public Iterator<Header> iterator() {
                return new EmptyIterator<Header>();
            }
        };
    }
}
