package com.example.backoffice.repository;

import com.example.backoffice.dao.DAO;
import com.example.backoffice.model.Token;

public class TokenRepository {

    public Token findByValue(String tokenValue) throws Exception {
        DAO dao = new DAO();
        try {
            dao.connect();
            String sql = "SELECT * FROM token WHERE token = ?";
            Token token = dao.get(sql, Token.class, tokenValue);
            return token;
        } catch(Exception e) {
            throw e;
        } finally {
            dao.close();
        }
    }
}