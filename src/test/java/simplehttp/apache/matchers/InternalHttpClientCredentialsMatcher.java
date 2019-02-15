package simplehttp.apache.matchers;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.HttpClient;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.net.URL;
import java.util.Optional;

import static org.apache.http.auth.AuthScope.ANY_REALM;
import static org.apache.http.auth.AuthScope.ANY_SCHEME;
import static org.apache.http.impl.client.ReflectiveInternalHttpClientAdapter.*;

public class InternalHttpClientCredentialsMatcher extends TypeSafeDiagnosingMatcher<HttpClient> {

	private final String username;
	private final String password;
	private final AuthScope scope;

	public static Matcher<HttpClient> credentialsProviderContains(URL url, String username, String password) {
		return new InternalHttpClientCredentialsMatcher(url, username, password);
	}

	private InternalHttpClientCredentialsMatcher(URL url, String username, String password) {
		this.username = username;
		this.password = password;
		this.scope = new AuthScope(url.getHost(), url.getPort(), ANY_REALM, ANY_SCHEME);
	}

	@Override
	protected boolean matchesSafely(HttpClient actual, Description mismatch) {
		Optional<Boolean> match = getCredentialsProvider(actual, mismatch).map(provider -> {
			Credentials credentials = provider.getCredentials(scope);
			if (credentials == null) {
				mismatch.appendText("No credentials found for ").appendValue(scope);
				return false;
			}
			if (!username.equals(credentials.getUserPrincipal().getName())) {
				mismatch.appendText("found username ").appendValue(credentials.getUserPrincipal().getName());
				return false;
			}
			if (!password.equals(credentials.getPassword())) {
				mismatch.appendText("found password ").appendValue(credentials.getPassword());
				return false;
			}
			return true;
		});
		return match.orElse(false);
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("containing ").appendValue(username).appendText(" ").appendValue(password).appendText(" for AuthScope ").appendValue(scope);

	}
}
