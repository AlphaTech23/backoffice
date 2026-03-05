package com.example.backoffice.service;

import java.time.LocalDateTime;

import com.example.backoffice.model.Token;
import com.example.backoffice.repository.TokenRepository;

public class TokenService {

    private TokenRepository tokenRepository;

    public TokenService() {
        this.tokenRepository = new TokenRepository();
    }

    public boolean isValid(String tokenValue) throws Exception {
        Token token = tokenRepository.findByValue(tokenValue);

        if (token == null)
            return false;

        return token.getDateExpiration().isAfter(LocalDateTime.now());
    }
}
