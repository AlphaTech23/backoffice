package com.example.backoffice.repository;

import com.example.backoffice.dao.DAO;
import com.example.backoffice.model.Parametre;

public class ParametreRepository {
    private final DAO dao;

    public ParametreRepository(DAO dao) {
        this.dao = dao;
    }

    public Double getVitesseMoyenne() throws Exception {

        String sql = "SELECT * FROM parametre WHERE cle = 'vitesse_moyenne'";

        Parametre vm = dao.get(sql, Parametre.class);

        return vm != null && vm.getValeur() != null && !vm.getValeur().isEmpty() 
                ? Double.parseDouble(vm.getValeur()) : null;
    }
}