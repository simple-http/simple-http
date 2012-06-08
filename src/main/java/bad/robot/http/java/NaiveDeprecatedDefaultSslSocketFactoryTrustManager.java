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

package bad.robot.http.java;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class NaiveDeprecatedDefaultSslSocketFactoryTrustManager implements ConfigureTrustManager {

    @Override
    public void configurePlatformTrustManager() {
        try {
            com.sun.net.ssl.SSLContext context = com.sun.net.ssl.SSLContext.getInstance("SSL");
            context.init(null, new com.sun.net.ssl.TrustManager[]{new FakeX509TrustManager()}, new SecureRandom());
            com.sun.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        } catch (GeneralSecurityException gse) {
            throw new IllegalStateException(gse.getMessage());
        }
    }

    private static class FakeX509TrustManager implements com.sun.net.ssl.X509TrustManager {
        private static final X509Certificate[] AcceptedIssuers = new X509Certificate[]{};

        public boolean isClientTrusted(X509Certificate[] chain) {
            return (true);
        }

        public boolean isServerTrusted(X509Certificate[] chain) {
            return (true);
        }

        public X509Certificate[] getAcceptedIssuers() {
            return (AcceptedIssuers);
        }
    }

}
