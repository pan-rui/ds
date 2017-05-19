package com.pc.security;

import org.apache.shiro.authc.HostAuthenticationToken;
import org.apache.shiro.authc.RememberMeAuthenticationToken;

/**
 * @Description: ${Description}
 * @Author: 潘锐 (2017-03-28 11:15)
 * @version: \$Rev: 1158 $
 * @UpdateAuthor: \$Author: panrui $
 * @UpdateDateTime: \$Date: 2017-04-18 15:53:47 +0800 (周二, 18 4月 2017) $
 */
public class AuthToken implements HostAuthenticationToken, RememberMeAuthenticationToken {

    private final String credential;
    private String username;
    private boolean rememberMe = false;
    private String token;
    private String host;

    public AuthToken(String username, String credential) {
        this(username, credential, false);
    }

    public AuthToken(String username, String credential, boolean rememberMe) {
        this.username = username;
        this.credential = credential;
        this.rememberMe = rememberMe;
    }

    @Override
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    @Override
    public Object getPrincipal() {
        return username;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCredential() {
        return credential;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
