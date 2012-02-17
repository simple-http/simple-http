package bad.robot.http.apache;

import org.apache.http.HttpEntity;

public interface HttpEntityConverter {
    HttpEntity asHttpEntity();
}
