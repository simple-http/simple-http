package bad.robot.http.apache;

import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.SchemeSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;

public enum Ssl {
    enabled {
        @Override
        public SchemeSocketFactory getSocketFactory() {
            return SSLSocketFactory.getSocketFactory();
        }
    },
    disabled {
        @Override
        public SchemeSocketFactory getSocketFactory() {
            return PlainSocketFactory.getSocketFactory();
        }
    };

    public abstract SchemeSocketFactory getSocketFactory();
}
