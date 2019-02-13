package simplehttp;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static simplehttp.CharacterSet.*;
import static simplehttp.EmptyHeaders.*;
import static simplehttp.HeaderList.*;
import static simplehttp.HeaderPair.*;

public class UnencodedStringMessageTest {

	@Test
	public void shouldContainContent() {
		assertThat(new UnencodedStringMessage("content").getContent(), is(new StringMessageContent("content")));
		assertThat(new UnencodedStringMessage("content", emptyHeaders()).getContent(), is(new StringMessageContent("content")));
		assertThat(new UnencodedStringMessage("content", emptyHeaders(), defaultCharacterSet).getContent(), is(new StringMessageContent("content")));
		assertThat(new UnencodedStringMessage("content", defaultCharacterSet).getContent(), is(new StringMessageContent("content")));
	}

	@Test
	public void shouldContainHeaders() {
		assertThat(new UnencodedStringMessage("n/a", emptyHeaders()).getHeaders(), is(emptyHeaders()));
		assertThat(new UnencodedStringMessage("n/a", defaultCharacterSet).getHeaders(), is(emptyHeaders()));
		assertThat(new UnencodedStringMessage("n/a", emptyHeaders(), defaultCharacterSet).getHeaders(), is(emptyHeaders()));
	}

	@Test
	public void stringRepresentation() {
		assertThat(new UnencodedStringMessage("content").toString(), is("UnencodedStringMessage{content='content', headers=''}"));
		assertThat(new UnencodedStringMessage("content", headers(header("a", "b"))).toString(), is("UnencodedStringMessage{content='content', headers='HeaderList{headers=[HeaderPair{name='a', value='b'}]}'}"));
		assertThat(new UnencodedStringMessage("content", headers(header("a", "b")), CharacterSet.UTF_8).toString(), is("UnencodedStringMessage{content='content', headers='HeaderList{headers=[HeaderPair{name='a', value='b'}]}'}"));
	}


}