/*
 * Copyright (c) 2011-2012, bad robot (london) ltd
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

package bad.robot.http;

/**
 * <p/>
 * A HTTP message consists of the following; a request line, such as GET /logo.gif HTTP/1.1 or Status line, such as HTTP/1.1 200 OK,
 * headers, an empty line and the optional HTTP message body data.
 * <p/>
 * <p>
 * This class represents a container to capture the {@link Headers} and optional {@link MessageContent} data.
 * </p>
 * See http://en.wikipedia.org/wiki/HTTP_body_data
 * See http://www.w3.org/Protocols/rfc2616/rfc2616-sec4.html#sec4.3
 */
public interface HttpPostMessage extends HttpRequest {

}