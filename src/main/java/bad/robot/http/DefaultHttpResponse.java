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

package bad.robot.http;

public class DefaultHttpResponse implements HttpResponse {

    private final int statusCode;
    private final String statusMessage;
    private final String content;
    private final Headers headers;

    public DefaultHttpResponse(int statusCode, String statusMessage, String content, Headers headers) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.content = content;
        this.headers = headers;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String getStatusMessage() {
        return statusMessage;
    }

    @Override
    public StringMessageContent getContent() {
        return new StringMessageContent(content);
    }

    @Override
    public Headers getHeaders() {
        return headers;
    }

    @Override
    public boolean ok() {
        return statusCode == 200;
    }

    @Override
    public String toString() {
        return "DefaultHttpResponse{statusCode=" + statusCode +
                ", statusMessage='" + statusMessage + '\'' +
                ", content='" + content + '\'' +
                ", headers='" + headers + '\'' +
                '}';
    }
}
