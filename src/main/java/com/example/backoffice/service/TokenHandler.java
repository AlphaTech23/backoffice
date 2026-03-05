package com.example.backoffice.service;

import com.example.framework.core.UserSession;

public class TokenHandler implements UserSession {
    
    private String token;
    private TokenService tokenService = new TokenService();
    
    public TokenHandler(String token) {
        this.token = token;
    }

    public TokenHandler() {
    }

    @Override
    public boolean isAuthentified() {
        try {
            return tokenService.isValid(token);  
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String[] getRoles() {
        return null;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
