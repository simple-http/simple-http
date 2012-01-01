/*
 * Copyright (c) 2009-2011, bad robot (london) ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package bad.robot.http.apache;

import bad.robot.http.HttpClient;
import bad.robot.http.HttpClientBuilder;
import bad.robot.http.HttpException;
import bad.robot.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import java.net.URL;
import java.util.concurrent.Callable;

import static com.google.code.tempusfugit.ExceptionWrapper.wrapAnyException;
import static com.google.code.tempusfugit.WithException.with;

public class ApacheHttpClient implements HttpClient {

    private final org.apache.http.client.HttpClient client;

    public ApacheHttpClient(HttpClientBuilder builder) {
        client = builder.build();
    }

    @Override
    public HttpResponse get(URL url) throws HttpException {
        HttpGet get = new HttpGet(url.toExternalForm());
        return wrapAnyException(execute(get), with(HttpException.class));
    }

    private Callable<HttpResponse> execute(final HttpUriRequest request) {
        return new Callable<HttpResponse>() {
            @Override
            public HttpResponse call() throws Exception {
                return client.execute(request, new HttpResponseHandler(new ToStringConsumer()));
            }
        };
    }

    @Override
    public void shutdown() {
        client.getConnectionManager().shutdown();
    }

}
