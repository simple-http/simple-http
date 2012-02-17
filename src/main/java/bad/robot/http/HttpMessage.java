package bad.robot.http;

/**
 * @see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec4.html">HTTP Specification (section 4)</a>
 */
public interface HttpMessage {

    MessageContent getContent();

    Headers getHeaders();

}
