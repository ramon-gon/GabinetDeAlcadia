package com.company.gabinetdealcadia.service;

import com.company.gabinetdealcadia.entity.Categoria;
import com.company.gabinetdealcadia.entity.Entitat;
import io.jmix.core.DataManager;
import io.jmix.core.FetchPlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service("gab_EntitatService")
public class EntitatService {

    @Autowired
    private DataManager dataManager;

    /**
     * LÓGICA DE VISTA DE LISTA:
     * Recarga la entidad con sus categorías y las devuelve unidas por comas.
     */
    public String obtenerNomsCategories(Entitat entitat) {
        if (entitat == null) return "";

        Entitat reloadEntitat = dataManager.load(Entitat.class)
                .id(entitat.getId())
                .fetchPlan(fetchPlan -> {
                    fetchPlan.addFetchPlan(FetchPlan.BASE);
                    fetchPlan.add("categories", FetchPlan.BASE);
                })
                .optional()
                .orElse(entitat);

        if (reloadEntitat.getCategories() == null || reloadEntitat.getCategories().isEmpty()) {
            return "";
        }

        return reloadEntitat.getCategories().stream()
                .map(Categoria::getNom)
                .collect(Collectors.joining(", "));
    }

    /**
     * LÓGICA DE VISTA DE DETALLE:
     * Carga todas las categorías disponibles para poblar el ComboBox.
     */
    public List<Categoria> cargarTodasLasCategorias() {
        return dataManager.load(Categoria.class)
                .all()
                .fetchPlan(FetchPlan.BASE)
                .list();
    }

    /**
     * LÓGICA DE VISTA DE DETALLE:
     * Sincroniza de forma segura la colección interna de Jmix con la selección del usuario.
     */
    public void sincronizarCategorias(Entitat entitat, Collection<Categoria> categoriasSeleccionadas) {
        if (entitat == null) return;

        if (entitat.getCategories() == null) {
            entitat.setCategories(new ArrayList<>());
        } else {
            entitat.getCategories().clear();
        }

        if (categoriasSeleccionadas != null) {
            entitat.getCategories().addAll(categoriasSeleccionadas);
        }
    }
}