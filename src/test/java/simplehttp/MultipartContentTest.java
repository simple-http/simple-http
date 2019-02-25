package simplehttp;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class MultipartContentTest {

	@Test
	public void hashCodeAndEquals() {
		EqualsVerifier.forClass(MultipartContent.class).verify();
	}
}