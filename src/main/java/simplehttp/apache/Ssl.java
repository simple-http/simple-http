/*
 * Copyright (c) 2011-2018, simple-http committers
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

import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import simplehttp.java.AlwaysTrustingDefaultSslSocketFactory;
import simplehttp.java.AlwaysTrustingHostNameVerifier;

public enum Ssl {
    enabled {
        @Override
        public ConnectionSocketFactory getSocketFactory() {
            return SSLConnectionSocketFactory.getSocketFactory();
        }
    },
    naive {
        @Override
        public ConnectionSocketFactory getSocketFactory() {
            setPlatformSslToAlwaysTrustCertificatesAndHosts();
            return SSLConnectionSocketFactory.getSocketFactory();
        }

        private void setPlatformSslToAlwaysTrustCertificatesAndHosts() {
            new AlwaysTrustingDefaultSslSocketFactory().configureDefaultSslSocketFactory();
            new AlwaysTrustingHostNameVerifier().configureHttpsUrlConnection();
        }
    },
    disabled {
        @Override
        public ConnectionSocketFactory getSocketFactory() {
            return PlainConnectionSocketFactory.getSocketFactory();
        }
    };

    public abstract ConnectionSocketFactory getSocketFactory();
}
