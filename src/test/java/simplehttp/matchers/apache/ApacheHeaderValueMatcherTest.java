package simplehttp.matchers.apache;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.hamcrest.StringDescription;
import org.junit.Test;
import simplehttp.matchers.Matchers;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class ApacheHeaderValueMatcherTest {

	private final Header header = new BasicHeader("Accept", "application/json, q=1; text/plain, q=0.5");

	@Test
	public void exampleUsage() {
		assertThat(header, is(Matchers.apacheHeader("Accept", is("application/json, q=1; text/plain, q=0.5"))));
	}

	@Test
	public void matches() {
		assertThat(Matchers.apacheHeader("Accept", is("application/json, q=1; text/plain, q=0.5")).matches(header), is(true));
		assertThat(Matchers.apacheHeader("Accept", is(not("application/json, q=1; text/plain, q=1.0"))).matches(header), is(true));
	}

	@Test
	public void doesNotMatch() {
		assertThat(Matchers.apacheHeader("Accept", containsString("xml")).matches(header), is(false));
		assertThat(Matchers.apacheHeader("accept", is("application/json, q=1; text/plain, q=0.5")).matches(header), is(false));
	}

	@Test
	public void description() {
		StringDescription description = new StringDescription();
		Matchers.apacheHeader("Accept", is("application/json, q=1; text/plain, q=0.5")).describeTo(description);
		assertThat(description.toString(), is("header \"Accept\" with matcher is \"application/json, q=1; text/plain, q=0.5\""));
	}
	
	@Test
	public void descriptionWhenMismatch() {
		StringDescription description = new StringDescription();
		Matchers.apacheHeader("Accept", is(not("application/json, q=1; text/plain, q=0.5"))).describeTo(description);
		assertThat(description.toString(), is("header \"Accept\" with matcher is not \"application/json, q=1; text/plain, q=0.5\""));
	}
}