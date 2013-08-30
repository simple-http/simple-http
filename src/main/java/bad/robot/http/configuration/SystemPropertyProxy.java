package bad.robot.http.configuration;

import bad.robot.http.HttpException;

import java.net.MalformedURLException;
import java.net.URL;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class SystemPropertyProxy extends Proxy {

    private static final String proxyUrl = "http.proxyHost";
    private static final String port = "http.proxyPort";
    private static final String user = "http.proxyUser";
    private static final String password = "http.proxyPassword";

    public static Proxy systemPropertyProxy() {
        try {
            return new SystemPropertyProxy(getUrlFromSystemProperties());
        } catch (MalformedURLException e) {
            throw new HttpException(e);
        }
    }

    private SystemPropertyProxy(URL value) {
        super(value);
    }

    private static URL getUrlFromSystemProperties() throws MalformedURLException {
        String url = (String) System.getProperties().get(SystemPropertyProxy.proxyUrl);
        String port = (String) System.getProperties().get(SystemPropertyProxy.port);
        if (isEmpty(url) || isEmpty(port))
            throw new IllegalArgumentException(format("'%s' and/or '%s' were not specified as system properties, use -D or set programatically", SystemPropertyProxy.proxyUrl, SystemPropertyProxy.port));
        if (url.toLowerCase().contains("://"))
            throw new IllegalArgumentException(format("no need to set a protocol on the proxy URL specified by the system property '%s'", proxyUrl));
        String username = (String) System.getProperties().get(SystemPropertyProxy.user);
        String password = (String) System.getProperties().get(SystemPropertyProxy.password);
        if (isEmpty(username) || isEmpty(password))
            return new URL(format("http://%s:%s", url, port));
       return new URL(format("http://%s:%s@%s:%s", username, password, url, port));
    }
}
