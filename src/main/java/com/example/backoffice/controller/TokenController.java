package com.example.backoffice.controller;

import java.util.Map;

import com.example.backoffice.service.TokenHandler;
import com.example.framework.annotations.Controller;
import com.example.framework.annotations.PostMapping;
import com.example.framework.annotations.Session;

@Controller
public class TokenController {
    
    @PostMapping("/api/authentification")
    public void authentification(@Session Map<String, Object> session, String token) {
        TokenHandler tokenHandler = new TokenHandler(token);
        session.put("token", tokenHandler.getToken());
    }
}
