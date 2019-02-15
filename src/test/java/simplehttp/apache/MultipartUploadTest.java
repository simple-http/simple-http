package simplehttp.apache;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import simplehttp.HttpClient;
import simplehttp.HttpException;
import simplehttp.Multipart;

import java.io.File;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.fail;
import static simplehttp.HttpClients.anApacheClient;
import static simplehttp.Url.url;

public class MultipartUploadTest {

	private final static WireMockServer server = new WireMockServer();
	private final HttpClient http = anApacheClient();

	@BeforeClass
	public static void startHttpServer() {
		WireMock.configureFor("localhost", 8080);
		server.start();
	}

	@AfterClass
	public static void stopHttpServer() {
		server.stop();
	}

	@Before
	public void resetHttpExpectations() {
		WireMock.reset();
	}

	@Test
	public void contentTypeHeaderIsSet() {
		http.post(url("http://localhost:8080/test"), new Multipart("example", new File("src/test/resource/example-image.png")));
		verify(postRequestedFor(urlEqualTo("/test")).withHeader("Content-Type", containing("multipart/form-data; boundary=")));
	}

	@Test
	public void multipartsInTheBodyAreSet() {
		http.post(url("http://localhost:8080/test"), new Multipart("example", new File("src/test/resource/example-image.png")));
		String expected = "Content-Disposition: form-data; name=\"example\"; filename=\"example-image.png\"\r\nContent-Type: application/octet-stream";
		verify(postRequestedFor(urlEqualTo("/test")).withRequestBody(containing(expected)));
	}	
	
	@Test
	public void fileDoesntExist() {
		try {
			http.post(url("http://localhost:8080/test"), new Multipart("example", new File("missing.png")));
			fail("Exception expected but never thrown");
		} catch (HttpException ignored) {
		} finally{
			verify(exactly(0), postRequestedFor(urlEqualTo("/test")));
		}
	}
}
