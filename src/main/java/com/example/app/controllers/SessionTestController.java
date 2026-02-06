package com.example.app.controllers;

import com.example.framework.annotations.Controller;
import com.example.framework.annotations.GetMapping;
import com.example.framework.annotations.RequestParam;
import com.example.framework.annotations.Session;

import java.util.Map;

@Controller
public class SessionTestController {

    /**
     * Ajoute ou modifie une valeur en session
     * Exemple :
     *   /session/set?key=user&value=Myah
     */
    @GetMapping("/session/set")
    public String setSession(
            @RequestParam("key") String key,
            @RequestParam("value") String value,
            @Session Map<String, Object> session) {

        session.put(key, value);
        return "Session SET : " + key + " = " + value;
    }

    /**
     * Lit une valeur depuis la session
     * Exemple :
     *   /session/get?key=user
     */
    @GetMapping("/session/get")
    public String getSession(
            @RequestParam("key") String key,
            @Session Map<String, Object> session) {

        Object value = session.get(key);
        return "Session GET : " + key + " = " + value;
    }

    /**
     * Supprime une clé de la session
     * Exemple :
     *   /session/remove?key=user
     */
    @GetMapping("/session/remove")
    public String removeSession(
            @RequestParam("key") String key,
            @Session Map<String, Object> session) {

        session.remove(key);
        return "Session REMOVE : " + key;
    }

    /**
     * Affiche toute la session
     * Exemple :
     *   /session/all
     */
    @GetMapping("/session/all")
    public String allSession(@Session Map<String, Object> session) {

        if (session.isEmpty()) {
            return "Session vide";
        }

        StringBuilder sb = new StringBuilder("Session content:\n");
        session.forEach((k, v) ->
                sb.append(k).append(" = ").append(v).append("\n")
        );

        return sb.toString();
    }

    /**
     * Vide complètement la session
     * Exemple :
     *   /session/clear
     */
    @GetMapping("/session/clear")
    public String clearSession(@Session Map<String, Object> session) {

        session.clear();
        return "Session cleared";
    }
}
