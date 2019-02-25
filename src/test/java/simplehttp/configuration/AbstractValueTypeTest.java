package simplehttp.configuration;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class AbstractValueTypeTest {

	@Test
	public void hashCodeAndEquals() {
		EqualsVerifier.forClass(AbstractValueType.class).verify();
	}

}