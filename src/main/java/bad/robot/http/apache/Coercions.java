package bad.robot.http.apache;

import bad.robot.http.Header;
import bad.robot.http.Headers;
import jedi.functional.Functor;
import org.apache.http.message.BasicHeader;

import java.util.List;

import static bad.robot.http.SimpleHeader.header;
import static bad.robot.http.SimpleHeaders.headers;
import static jedi.functional.FunctionalPrimitives.collect;

public class Coercions {

    static org.apache.http.Header[] asApacheBasicHeader(Headers headers) {
        return collect(headers, new Functor<Header, org.apache.http.Header>() {
            @Override
            public org.apache.http.Header execute(Header header) {
                return new BasicHeader(header.name(), header.value());
            }
        }).toArray(new org.apache.http.Header[]{});
    }
    
    static Headers asHeaders(org.apache.http.Header[] headers) {
        List<Header> list = collect(headers, new Functor<org.apache.http.Header, Header>() {
            @Override
            public Header execute(org.apache.http.Header header) {
                return header(header.getName(), header.getValue());
            }
        });
        return headers(list.toArray(new Header[list.size()]));
    }
}
