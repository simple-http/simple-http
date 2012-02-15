package bad.robot.http.apache;

import bad.robot.http.HttpResponse;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static bad.robot.http.SimpleHeader.header;
import static bad.robot.http.matchers.Matchers.*;
import static org.apache.http.HttpVersion.HTTP_1_1;
import static org.junit.Assert.assertThat;

@RunWith(JMock.class)
public class HttpResponseHandlerTest {

    private final Mockery context = new Mockery();

    private final HttpEntity body = context.mock(HttpEntity.class);

    private final BasicHttpResponse anApacheOkResponse = new BasicHttpResponse(new BasicStatusLine(HTTP_1_1, 200, "OK"));
    private final BasicHttpResponse anApacheResponseWithHeaders = new BasicHttpResponse(new BasicStatusLine(HTTP_1_1, 200, "OK")) {{
        setHeaders(new Header[]{ new BasicHeader("Accept", "text/html"), new BasicHeader("Content-Type", "application/json")});
    }};
    private final BasicHttpResponse anApacheResponseWithBody = new BasicHttpResponse(new BasicStatusLine(HTTP_1_1, 400, "Bad Request")) {{
        setEntity(body);
    }};

    private final ContentConsumingStrategy consumer = context.mock(ContentConsumingStrategy.class);
    private final HttpResponseHandler handler = new HttpResponseHandler(consumer);

    @Test
    public void shouldConvertStatusCode() throws IOException {
        HttpResponse response = handler.handleResponse(anApacheOkResponse);
        assertThat(response, hasStatus(200));
    }

    @Test
    public void shouldConvertStatusMessage() throws IOException {
        HttpResponse response = handler.handleResponse(anApacheOkResponse);
        assertThat(response, hasStatusMessage("OK"));
    }

    @Test
    public void shouldConvertEmptyBody() throws IOException {
        HttpResponse response = handler.handleResponse(anApacheOkResponse);
        assertThat(response, hasBody(""));
    }

    @Test
    public void shouldConvertBody() throws IOException {
        context.checking(new Expectations(){{
            one(consumer).toString(body); will(returnValue("I'm a http message body"));
            ignoring(body).consumeContent();
        }});
        HttpResponse response = handler.handleResponse(anApacheResponseWithBody);
        assertThat(response, hasBody("I'm a http message body"));
    }

    @Test
    public void shouldConvertHeaders() throws IOException {
        HttpResponse response = handler.handleResponse(anApacheResponseWithHeaders);
        assertThat(response, hasHeader(header("Accept", "text/html")));
        assertThat(response, hasHeader(header("Content-Type", "application/json")));
    }

    @Test
    public void shouldCleanupContent() throws IOException {
        context.checking(new Expectations(){{
            ignoring(consumer);
            one(body).consumeContent();
        }});
        handler.handleResponse(anApacheResponseWithBody);
    }

    @Test (expected = IOException.class)
    public void shouldCleanupContentEvenOnException() throws IOException {
        context.checking(new Expectations(){{
            allowing(consumer); will(throwException(new IOException()));
            one(body).consumeContent();
        }});
        handler.handleResponse(anApacheResponseWithBody);
    }

}
