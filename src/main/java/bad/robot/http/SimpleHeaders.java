package bad.robot.http;

import jedi.functional.EmptyIterator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

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

    @Override
    public int hashCode() {
        return reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object that) {
        return reflectionEquals(this, that);
    }

    @Override
    public String toString() {
        return "SimpleHeaders{headers=" + headers + '}';
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
