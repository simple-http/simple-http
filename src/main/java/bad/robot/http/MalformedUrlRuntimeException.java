package bad.robot.http;

import java.net.MalformedURLException;

public class MalformedUrlRuntimeException extends HttpException {
    public MalformedUrlRuntimeException(MalformedURLException e) {
        super(e);
    }
}
