package bad.robot.http;

/**
 * <p/>
 * A HTTP message consists of the following; a request line, such as GET /logo.gif HTTP/1.1 or Status line, such as HTTP/1.1 200 OK,
 * headers, an empty line and the optional HTTP message body data.
 * <p/>
 * <p>
 * This class represents a container to capture the {@link Headers} and optional {@link MessageContent} data.
 * </p>
 * See http://en.wikipedia.org/wiki/HTTP_body_data
 * See http://www.w3.org/Protocols/rfc2616/rfc2616-sec4.html#sec4.3
 */
public interface HttpPostMessage extends HttpRequest {

}