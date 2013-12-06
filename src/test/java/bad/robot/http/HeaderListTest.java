package bad.robot.http;

import org.junit.Test;

import static bad.robot.http.HeaderList.headers;
import static bad.robot.http.HeaderPair.header;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class HeaderListTest {

    @Test
    public void has() {
        Headers headers = headers(header("accept", "application/json"));
        assertThat(headers.has("accept"), is(true));
        assertThat(headers.has("Accept"), is(true));
    }

    @Test
    public void hasNot() {
        Headers headers = headers(header("cheese", "ham"));
        assertThat(headers.has("accept"), is(false));
    }

    @Test
    public void getHeader() {
        Headers headers = headers(header("cheese", "ham"));
        assertThat(headers.get("cheese"), is(header("cheese", "ham")));
    }

}
