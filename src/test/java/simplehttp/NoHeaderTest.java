package simplehttp;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class NoHeaderTest {

	@Test
	public void noHeaderInstancesShouldBeEqual() {
		assertThat(new NoHeader(), is(new NoHeader()));
	}

	@Test
	public void hashCodeAndEquals() {
		EqualsVerifier.forClass(NoHeader.class).verify();
	}

}