package simplehttp.apache;

import org.apache.http.client.config.RequestConfig;

public class ApacheConfig {

	private RequestConfig config;

	public ApacheConfig(RequestConfig config) {
		this.config = config;
	}

	public RequestConfig requestConfig() {
		return config;
	}
}
