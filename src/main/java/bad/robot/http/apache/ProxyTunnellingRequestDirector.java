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

package bad.robot.http.apache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.*;
import org.apache.http.auth.*;
import org.apache.http.client.*;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultRequestDirector;
import org.apache.http.impl.client.TunnelRefusedException;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

class ProxyTunnellingRequestDirector extends DefaultRequestDirector {

    private final Log log;

    public ProxyTunnellingRequestDirector(HttpRequestExecutor requestExec, ClientConnectionManager conman, ConnectionReuseStrategy reustrat, ConnectionKeepAliveStrategy kastrat, HttpRoutePlanner rouplan, HttpProcessor httpProcessor, HttpRequestRetryHandler retryHandler, RedirectStrategy redirectStrategy, AuthenticationHandler targetAuthHandler, AuthenticationHandler proxyAuthHandler, UserTokenHandler stateHandler, HttpParams params) {
        super(LogFactory.getLog(AbstractHttpClient.class), requestExec, conman, reustrat, kastrat, rouplan, httpProcessor, retryHandler, redirectStrategy, targetAuthHandler, proxyAuthHandler, stateHandler, params);
        log = LogFactory.getLog(AbstractHttpClient.class);
    }

    @Override
    protected boolean createTunnelToProxy(HttpRoute route, int hop, HttpContext context) throws HttpException, IOException {
        HttpHost proxy = route.getProxyHost(); // route.getProxyHost() // use hop?
        HttpHost target = route.getTargetHost();
        HttpResponse response = null;

        boolean done = false;
        while (!done) {

            done = true;

            if (!managedConn.isOpen()) {
                managedConn.open(route, context, params);
            }

            HttpRequest connect = createConnectRequest(route, context);
            connect.setParams(params);

            // Populate the execution context
            context.setAttribute(ExecutionContext.HTTP_TARGET_HOST, target);
            context.setAttribute(ExecutionContext.HTTP_PROXY_HOST, proxy);
            context.setAttribute(ExecutionContext.HTTP_CONNECTION, managedConn);
            context.setAttribute(ExecutionContext.HTTP_REQUEST, connect);
            context.setAttribute(ClientContext.TARGET_AUTH_STATE, targetAuthState);
            context.setAttribute(ClientContext.PROXY_AUTH_STATE, proxyAuthState);

            requestExec.preProcess(connect, httpProcessor, context);

            System.out.println(connect + " " + proxy);
            response = requestExec.execute(connect, managedConn, context);

            response.setParams(params);
            requestExec.postProcess(response, httpProcessor, context);

            int status = response.getStatusLine().getStatusCode();
            if (status < 200) {
                throw new HttpException("Unexpected response to CONNECT request: " + response.getStatusLine());
            }

            CredentialsProvider credentialsProvider = (CredentialsProvider) context.getAttribute(ClientContext.CREDS_PROVIDER);

            if (credentialsProvider != null && HttpClientParams.isAuthenticating(params)) {
                if (proxyAuthHandler.isAuthenticationRequested(response, context)) {

                    log.debug("Proxy requested authentication");
                    Map<String, Header> challenges = proxyAuthHandler.getChallenges(response, context);
                    try {
                        processChallenges(challenges, proxyAuthState, proxyAuthHandler, response, context);
                    } catch (AuthenticationException ex) {
                        if (log.isWarnEnabled()) {
                            log.warn("Authentication error: " + ex.getMessage());
                        }
                        break;
                    }
                    updateAuthState(proxyAuthState, proxy, credentialsProvider);

                    if (proxyAuthState.getCredentials() != null) {
                        done = false;

                        // Retry request
                        if (reuseStrategy.keepAlive(response, context)) {
                            log.debug("Connection kept alive");
                            // Consume response content
                            HttpEntity entity = response.getEntity();
                            EntityUtils.consume(entity);
                        } else {
                            managedConn.close();
                        }

                    }

                } else {
                    // Reset proxy auth scope
                    proxyAuthState.setAuthScope(null);
                }
            }
        }

        int status = response.getStatusLine().getStatusCode(); // can't be null

        if (status > 299) {

            // Buffer response content
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                response.setEntity(new BufferedHttpEntity(entity));
            }

            managedConn.close();
            throw new TunnelRefusedException("CONNECT refused by proxy: " + response.getStatusLine(), response);
        }

        managedConn.markReusable();

        // How to decide on security of the tunnelled connection?
        // The socket factory knows only about the segment to the proxy.
        // Even if that is secure, the hop to the target may be insecure.
        // Leave it to derived classes, consider insecure by default here.
        return false;

    }

    private void processChallenges(Map<String, Header> challenges, AuthState authState, AuthenticationHandler authHandler, HttpResponse response, HttpContext context) throws MalformedChallengeException, AuthenticationException {

        AuthScheme authScheme = authState.getAuthScheme();
        if (authScheme == null) {
            // Authentication not attempted before
            authScheme = authHandler.selectScheme(challenges, response, context);
            authState.setAuthScheme(authScheme);
        }
        String id = authScheme.getSchemeName();

        Header challenge = challenges.get(id.toLowerCase(Locale.ENGLISH));
        if (challenge == null)
            throw new AuthenticationException(id + " authorization challenge expected, but not found");
        authScheme.processChallenge(challenge);
    }

    private void updateAuthState(AuthState authState, HttpHost host, CredentialsProvider credsProvider) {
        if (!authState.isValid())
            return;

        String hostname = host.getHostName();
        int port = host.getPort();
        if (port < 0) {
            Scheme scheme = connManager.getSchemeRegistry().getScheme(host);
            port = scheme.getDefaultPort();
        }

        AuthScheme authScheme = authState.getAuthScheme();
        AuthScope authScope = new AuthScope(hostname, port, authScheme.getRealm(), authScheme.getSchemeName());

        Credentials credentials = authState.getCredentials();
        if (credentials == null) {
            credentials = credsProvider.getCredentials(authScope);
        } else {
            if (authScheme.isComplete()) {
                this.log.debug("Authentication failed");
                credentials = null;
            }
        }
        authState.setAuthScope(authScope);
        authState.setCredentials(credentials);
    }


}
