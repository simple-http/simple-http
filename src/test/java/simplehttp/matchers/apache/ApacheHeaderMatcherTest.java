package simplehttp.matchers.apache;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.hamcrest.StringDescription;
import org.junit.Test;
import simplehttp.matchers.Matchers;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ApacheHeaderMatcherTest {

	private final Header header = new BasicHeader("Accept", "application/json, q=1; text/plain, q=0.5");

	@Test
	public void exampleUsage() {
		assertThat(header, Matchers.apacheHeader("Accept", "application/json, q=1; text/plain, q=0.5"));
	}

	@Test
	public void matches() {
		assertThat(Matchers.apacheHeader("Accept", "application/json, q=1; text/plain, q=0.5").matches(header), is(true));
	}

	@Test
	public void doesNotMatch() {
		assertThat(Matchers.apacheHeader("Accept", "xml").matches(header), is(false));
		assertThat(Matchers.apacheHeader("accept", "json").matches(header), is(false));
	}

	@Test
	public void description() {
		StringDescription description = new StringDescription();
		Matchers.apacheHeader("Accept", "application/json, q=1; text/plain, q=0.5").describeTo(description);
		assertThat(description.toString(), is("header \"Accept\" with value of \"application/json, q=1; text/plain, q=0.5\""));
	}

}