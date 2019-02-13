package simplehttp;

import org.junit.Test;

import java.io.File;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static simplehttp.EmptyHeaders.emptyHeaders;

public class MultipartTest {

	@Test
	public void shouldContainContent() {
		assertThat(new Multipart("example", new File("dunno.txt")).getContent(), is(new MultipartContent("example", new File("dunno.txt"))));
	}

	@Test
	public void shouldContainHeaders() {
		assertThat(new Multipart("example", new File("dunno.txt")).getHeaders(), is(emptyHeaders()));
	}

	@Test
	public void stringRepresentation() {
		assertThat(new Multipart("example", new File("dunno.txt")).toString(), is("Multipart{content='name=example, filename=dunno.txt', headers=''}"));
	}

}