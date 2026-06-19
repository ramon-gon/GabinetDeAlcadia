package com.company.gabinetdealcadia.service;

import com.company.gabinetdealcadia.entity.Carrec;
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
     * LÒGICA DE VISTA DE LLISTA:
     * Torna a carregar l'entitat amb les seves categories i les retorna unides per comes.
     */
    public String obtenirNomsCategories(Entitat entitat) {
        if (entitat == null) return "";

        Entitat entitatRecarregada = dataManager.load(Entitat.class)
                .id(entitat.getId())
                .fetchPlan(plaDeCarrega -> {
                    plaDeCarrega.addFetchPlan(FetchPlan.BASE);
                    plaDeCarrega.add("categories", FetchPlan.BASE);
                })
                .optional()
                .orElse(entitat);

        if (entitatRecarregada.getCategories() == null || entitatRecarregada.getCategories().isEmpty()) {
            return "";
        }

        return entitatRecarregada.getCategories().stream()
                .map(Categoria::getNom)
                .collect(Collectors.joining(", "));
    }

    /**
     * LÒGICA DE VISTA DE DETALL:
     * Carrega totes les categories disponibles per a omplir el ComboBox.
     */
    public List<Categoria> carregarTotesLesCategories() {
        return dataManager.load(Categoria.class)
                .all()
                .fetchPlan(FetchPlan.BASE)
                .list();
    }

    /**
     * LÒGICA DE VISTA DE DETALL:
     * Sincronitza de forma segura la col·lecció interna de Jmix amb la selecció de l'usuari.
     */
    public void sincronitzarCategories(Entitat entitat, Collection<Categoria> categoriesSeleccionades) {
        if (entitat == null) return;

        if (entitat.getCategories() == null) {
            entitat.setCategories(new ArrayList<>());
        } else {
            entitat.getCategories().clear();
        }

        if (categoriesSeleccionades != null) {
            entitat.getCategories().addAll(categoriesSeleccionades);
        }
    }

    /**
     * 🛠️ LÒGICA DE VISTA DE LLISTA:
     * Cerca el president actiu i vigent d'una entitat a la taula Carrec
     * i en retorna el seu nom complet formatat.
     */
    public String obtenirNomPresident(Entitat entitat) {
        if (entitat == null) return "";

        // 🛠️ SOLUCIÓ: Utilitzem 'c.titolCarrec' (que és el nom real del camp a Java)
        // I filtrem també per 'c.vigent = true' perquè només agafi el president actual
        java.util.Optional<Carrec> carrecPresident = dataManager.load(Carrec.class)
                .query("select c from Carrec c where c.entitat = :entitat and c.vigent = true and (lower(c.titolCarrec) like 'president%')")
                .parameter("entitat", entitat)
                .optional();

        // Si trobem el càrrec, formatem el nom del Contacte associat
        if (carrecPresident.isPresent() && carrecPresident.get().getContacte() != null) {
            return formatarNomComplet(carrecPresident.get().getContacte());
        }

        return "-"; // Si no es troba cap president actiu, es mostra un guió
    }

    /**
     * Mètode auxiliar per a formatar el nom complet del contacte.
     */
    private String formatarNomComplet(com.company.gabinetdealcadia.entity.Contacte contacte) {
        if (contacte == null) return "";

        String nom = contacte.getNom() != null ? contacte.getNom() : "";
        String primerCognom = contacte.getPrimer_cognom() != null ? contacte.getPrimer_cognom() : "";
        String segonCognom = contacte.getSegon_cognom() != null ? contacte.getSegon_cognom() : "";

        return (nom + " " + primerCognom + " " + segonCognom).trim().replaceAll("\\s+", " ");
    }
}