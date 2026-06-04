package com.company.gabinetdealcadia.service;

import io.jmix.core.DataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EntitatService {

    @Autowired
    private DataManager dataManager;

    public List<String> obtenirCategoriesUniques() {
        return dataManager.loadValues(
                        "select distinct e.categoria from Entitat e where e.categoria is not null order by e.categoria")
                .properties("categoria")
                .list()
                .stream()
                .map(kv -> kv.<String>getValue("categoria"))
                .toList();
    }
}