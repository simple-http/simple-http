package simplehttp;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class StringMessageContentTest {

	@Test
	public void hashCodeAndEquals() {
		EqualsVerifier.forClass(StringMessageContent.class).verify();
	}
}