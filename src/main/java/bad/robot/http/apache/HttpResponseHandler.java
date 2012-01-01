/*
 * Copyright (c) 2009-2012, bad robot (london) ltd
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

import bad.robot.http.DefaultHttpResponse;
import bad.robot.http.HttpResponse;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;

import java.io.IOException;

class HttpResponseHandler implements ResponseHandler<HttpResponse> {

    private final ContentConsumingStrategy consumer;

    HttpResponseHandler(ContentConsumingStrategy consumer) {
        this.consumer = consumer;
    }

    @Override
    public HttpResponse handleResponse(org.apache.http.HttpResponse response) throws IOException {
        return new DefaultHttpResponse(getStatusCodeFrom(response), getStatusMessageFrom(response), getContentFrom(response));
    }

    private static String getStatusMessageFrom(org.apache.http.HttpResponse response) {
        return response.getStatusLine().getReasonPhrase();
    }

    private static int getStatusCodeFrom(org.apache.http.HttpResponse response) {
        return response.getStatusLine().getStatusCode();
    }

    private String getContentFrom(org.apache.http.HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        if (entity == null)
            return "";
        try {
            return consumer.toString(entity);
        } finally {
            cleanup(entity);
        }
    }

    private static void cleanup(HttpEntity entity) throws IOException {
        entity.consumeContent();
    }

}
