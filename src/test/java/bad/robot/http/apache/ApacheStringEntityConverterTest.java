package bad.robot.http.apache;

import bad.robot.http.FormUrlEncodedMessage;
import bad.robot.http.Tuples;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.junit.Test;

import java.io.IOException;

import static bad.robot.http.matchers.Matchers.apacheHeader;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ApacheStringEntityConverterTest {

    private static final String encodedContent = "name=I%27m+a+cheese+sandwich";

    @Test
    public void shouldConvertSimpleBody() throws IOException {
        Tuples tuples = new Tuples("name", "I'm a cheese sandwich");
        ApacheStringEntityConverter converter = new ApacheStringEntityConverter(new FormUrlEncodedMessage(tuples));
        HttpEntity entity = converter.asHttpEntity();

        String content = IOUtils.toString(entity.getContent());
        assertThat(content, is(encodedContent));
        assertThat(entity.getContentType(), is(apacheHeader("Content-Type", "application/x-www-form-urlencoded; charset=ISO-8859-1")));
        assertThat(entity.getContentLength(), is((long) encodedContent.getBytes().length));
    }

    @Test
    public void shouldNotAllowDuplicateKeys() throws IOException {
        Tuples tuples = new Tuples("name", "value", "name", "anotherValue");
        ApacheStringEntityConverter converter = new ApacheStringEntityConverter(new FormUrlEncodedMessage(tuples));
        HttpEntity entity = converter.asHttpEntity();

        String content = IOUtils.toString(entity.getContent());
        assertThat(content, is("name=anotherValue"));
    }

}
