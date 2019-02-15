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

package simplehttp.java;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class AlwaysTrustingDefaultSslSocketFactory {

    public void configureDefaultSslSocketFactory() {
        try {
            SSLContext context = SSLContext.getInstance("TLSv1.2");
            context.init(null, new TrustManager[]{new AlwaysTrustingX509TrustManager()}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    private static class AlwaysTrustingX509TrustManager implements X509TrustManager {

        private static final X509Certificate[] AcceptedIssuers = new X509Certificate[]{};

        public void checkClientTrusted(X509Certificate[] chain, String authType) { /* always proceed / trust */ }

        public void checkServerTrusted(X509Certificate[] chain, String authType) { /* always proceed / trust */ }

        public X509Certificate[] getAcceptedIssuers() {
            return (AcceptedIssuers);
        }
    }

}
