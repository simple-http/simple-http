package simplehttp;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static simplehttp.CharacterSet.defaultCharacterSet;
import static simplehttp.EmptyHeaders.emptyHeaders;
import static simplehttp.FormParameters.*;
import static simplehttp.HeaderList.headers;
import static simplehttp.HeaderPair.header;

public class FormUrlEncodedMessageTest {

	@Test
	public void shouldContainContent() {
		assertThat(new FormUrlEncodedMessage(params("name", "bob")).getContent(), is(params("name", "bob")));
		assertThat(new FormUrlEncodedMessage(params("name", "bob"), emptyHeaders()).getContent(), is(params("name", "bob")));
		assertThat(new FormUrlEncodedMessage(params("name", "bob"), emptyHeaders(), defaultCharacterSet).getContent(), is(params("name", "bob")));
		assertThat(new FormUrlEncodedMessage(params("name", "bob"), defaultCharacterSet).getContent(), is(params("name", "bob")));
	}

	@Test
	public void shouldContainHeaders() {
		assertThat(new FormUrlEncodedMessage(params("name", "bob"), emptyHeaders()).getHeaders(), is(emptyHeaders()));
		assertThat(new FormUrlEncodedMessage(params("name", "bob"), defaultCharacterSet).getHeaders(), is(emptyHeaders()));
		assertThat(new FormUrlEncodedMessage(params("name", "bob"), emptyHeaders(), defaultCharacterSet).getHeaders(), is(emptyHeaders()));
	}

	@Test
	public void stringRepresentation() {
		assertThat(new FormUrlEncodedMessage(params("name", "bob")).toString(), is("FormUrlEncodedMessage{content='name=bob', headers=''characterSet='ISO-8859-1'}"));
		assertThat(new FormUrlEncodedMessage(params("name", "bob"), headers(header("a", "b"))).toString(), is("FormUrlEncodedMessage{content='name=bob', headers='HeaderList{headers=[HeaderPair{name='a', value='b'}]}'characterSet='ISO-8859-1'}"));
		assertThat(new FormUrlEncodedMessage(params("name", "bob"), headers(header("a", "b")), CharacterSet.UTF_8).toString(), is("FormUrlEncodedMessage{content='name=bob', headers='HeaderList{headers=[HeaderPair{name='a', value='b'}]}'characterSet='UTF-8'}"));
	}

}