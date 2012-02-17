package bad.robot.http.apache;

import bad.robot.http.HttpException;
import bad.robot.http.HttpPostMessage;
import bad.robot.http.Transform;
import bad.robot.http.Tuples;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.Map;

class ApacheStringEntityConverter implements HttpEntityConverter {

    private final HttpPostMessage message;

    public ApacheStringEntityConverter(HttpPostMessage message) {
        this.message = message;
    }

    @Override
    public HttpEntity asHttpEntity() {
        try {
            Tuples content = (Tuples) message.getContent();
            return new UrlEncodedFormEntity(content.transform(asApacheNameValuePair()));
        } catch (UnsupportedEncodingException e) {
            throw new HttpException(e);
        }
    }

    private Transform<Map.Entry<String, String>, NameValuePair> asApacheNameValuePair() {
        return new Transform<Map.Entry<String, String>, NameValuePair>() {
            @Override
            public NameValuePair call(Map.Entry<String, String> tuple) {
                return new BasicNameValuePair(tuple.getKey(), tuple.getValue());
            }
        };
    }

}
