package com.example.backoffice.repository;

import com.example.backoffice.dao.DAO;
import com.example.backoffice.model.Token;

public class TokenRepository {

    public Token findByValue(String tokenValue) throws Exception {
        String sql = "SELECT * FROM token WHERE token = ?";
        return DAO.get(sql, Token.class, tokenValue);
    }
}