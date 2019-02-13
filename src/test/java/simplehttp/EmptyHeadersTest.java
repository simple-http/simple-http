package simplehttp;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static simplehttp.EmptyHeaders.*;

public class EmptyHeadersTest {

	@Test
	public void emptyHeadersInstancesShouldBeEqual() {
		assertThat(emptyHeaders(), is(emptyHeaders()));
	}


}