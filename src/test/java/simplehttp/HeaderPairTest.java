package simplehttp;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class HeaderPairTest {

	@Test
	public void hashCodeAndEquals() {
		EqualsVerifier.forClass(HeaderPair.class).verify();
	}

}