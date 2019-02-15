/*
 * Copyright (c) 2011-2019, simple-http committers
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

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
