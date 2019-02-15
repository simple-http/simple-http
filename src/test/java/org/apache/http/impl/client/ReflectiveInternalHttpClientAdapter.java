package org.apache.http.impl.client;

import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.hamcrest.Description;

import java.lang.reflect.Field;
import java.util.Optional;

public class ReflectiveInternalHttpClientAdapter {

	public static Optional<CredentialsProvider> getCredentialsProvider(HttpClient actual, Description mismatch) {
		try {
			InternalHttpClient client = (InternalHttpClient) actual;
			Field field = InternalHttpClient.class.getDeclaredField("credentialsProvider");
			field.setAccessible(true);
			return Optional.ofNullable((CredentialsProvider) field.get(client));
		} catch (ClassCastException e) {
			mismatch.appendText("Expecting HttpClient to cast to InternalHttpClient or credentials cast toe CredentialsProvider, can not check internals");
			return Optional.empty();
		} catch (NoSuchFieldException e) {
			mismatch.appendText("Expecting InternalHttpClient to contain a field called 'credentialsProvider', can not check internals");
			return Optional.empty();
		} catch (IllegalAccessException e) {
			mismatch.appendText("Unable to access the 'credentialsProvider' field, can not check internals");
			return Optional.empty();
		}
	}

	public static Optional<RequestConfig> getConfig(HttpClient actual, Description mismatch) {
		try {
			InternalHttpClient client = (InternalHttpClient) actual;
			return Optional.ofNullable(client.getConfig());
		} catch (ClassCastException e) {
			mismatch.appendText("Expecting HttpClient to cast to InternalHttpClient, can not check internals");
			return Optional.empty();
		}
	}
}
