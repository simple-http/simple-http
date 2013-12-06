package bad.robot.http;

import java.net.MalformedURLException;
import java.net.URL;

public class Url {
    public static URL url(String href) {
        try {
            return new URL(href);
        } catch (MalformedURLException e) {
            throw new MalformedUrlRuntimeException(e);
        }
    }

}
