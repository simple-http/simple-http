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

import java.net.MalformedURLException;
import java.net.URL;

public class Url {

    private final URL url;

    public Url(URL url) {
        this.url = url;
    }

    public Url(String url) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            throw new HttpException(e);
        }
    }

    public String toExternalForm() {
        return url.toExternalForm();
    }

    public URL toURL() {
        return url;
    }
}
