package bad.robot.http.apache;

import bad.robot.http.Header;
import bad.robot.http.Headers;
import jedi.functional.Functor;
import org.apache.http.message.BasicHeader;

import static jedi.functional.FunctionalPrimitives.collect;

public class Coercions {

    static org.apache.http.Header[] asApacheBufferedHeader(Headers headers) {
        return collect(headers, new Functor<Header, org.apache.http.Header>() {
            @Override
            public org.apache.http.Header execute(Header header) {
                return new BasicHeader(header.name(), header.value());
            }
        }).toArray(new org.apache.http.Header[]{});
    }
}
