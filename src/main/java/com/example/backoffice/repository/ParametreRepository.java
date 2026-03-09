package com.example.backoffice.repository;

import com.example.backoffice.dao.DAO;
import com.example.backoffice.model.Parametre;

public class ParametreRepository {

    public Double getVitesseMoyenne() throws Exception {

        String sql = "SELECT * FROM parametre WHERE cle = 'vitesse_moyenne'";

        Parametre vm = DAO.get(sql, Parametre.class);

        return vm != null && vm.getValeur() != null && !vm.getValeur().isEmpty() 
                ? Double.parseDouble(vm.getValeur()) : null;
    }
}