package com.example.backoffice.repository;

import com.example.backoffice.dao.DAO;

public class ParametreRepository {

    public Double getVitesseMoyenne() throws Exception {

        String sql = "SELECT valeur FROM parametre WHERE cle = 'vitesse_moyenne'";

        String valeur = DAO.get(sql, String.class);

        return valeur != null ? Double.parseDouble(valeur) : null;
    }
}