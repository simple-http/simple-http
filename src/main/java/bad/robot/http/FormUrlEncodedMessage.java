package bad.robot.http;

import static bad.robot.http.SimpleHeaders.noHeaders;

public class FormUrlEncodedMessage implements HttpPostMessage {

    private final Headers headers;
    private final Tuples content;

    public FormUrlEncodedMessage(Tuples content) {
        this(content, noHeaders());
    }

    public FormUrlEncodedMessage(Tuples content, Headers headers) {
        this.headers = headers;
        this.content = content;
    }

    @Override
    public Headers getHeaders() {
        // Content-Type and Content-Length are already set in Tuples (at least for Apache)
        return headers;
    }

    @Override
    public Tuples getContent() {
        return content;
    }

}
