package com.company.gabinetdealcadia.service;

import com.company.gabinetdealcadia.entity.Carrec;
import com.company.gabinetdealcadia.entity.Categoria;
import com.company.gabinetdealcadia.entity.Contacte;
import io.jmix.core.DataManager;
import io.jmix.core.FetchPlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("gab_ContacteService")
public class ContacteService {

    @Autowired
    private DataManager dataManager;

    /**
     * Contenedor de datos procesados listos para los campos de texto del detalle.
     */
    public static class DetallCampsProcesats {
        private final String entitats;
        private final String titols;
        private final String categories;

        public DetallCampsProcesats(String entitats, String titols, String categories) {
            this.entitats = entitats;
            this.titols = titols;
            this.categories = categories;
        }

        public String getEntitats() { return entitats; }
        public String getTitols() { return titols; }
        public String getCategories() { return categories; }
    }

    /**
     * LÓGICA DE VISTA DE LISTA:
     * Obtiene los nombres unificados de las entidades asociadas a los cargos de un contacto.
     */
    public String obtenerEntitatsPertanyents(Contacte contacte) {
        if (contacte == null) return "";

        List<Carrec> totsElsCarrecs = dataManager.load(Carrec.class)
                .query("select c from Carrec c where c.contacte = :contacte")
                .parameter("contacte", contacte)
                .fetchPlan(fetchPlan -> {
                    fetchPlan.addFetchPlan(FetchPlan.BASE);
                    fetchPlan.add("entitat", FetchPlan.BASE);
                })
                .list();

        if (totsElsCarrecs.isEmpty()) {
            return "";
        }

        return totsElsCarrecs.stream()
                .filter(carrec -> carrec.getEntitat() != null)
                .map(carrec -> carrec.getEntitat().getNom())
                .distinct()
                .collect(Collectors.joining(", "));
    }

    /**
     * LÓGICA DE VISTA DE LISTA:
     * Unifica y ordena de forma óptima las categorías de la entidad y las propias del contacto.
     */
    public String obtenerCategoriesUnificades(Contacte contacte) {
        if (contacte == null) return "";

        List<Carrec> totsElsCarrecs = dataManager.load(Carrec.class)
                .query("select c from Carrec c where c.contacte = :contacte")
                .parameter("contacte", contacte)
                .fetchPlan(fp -> {
                    fp.addFetchPlan(FetchPlan.BASE);
                    fp.add("entitat", FetchPlan.BASE);
                    fp.add("entitat.categories", FetchPlan.BASE);
                })
                .list();

        Contacte reloadContacte = dataManager.load(Contacte.class)
                .id(contacte.getId())
                .fetchPlan(fp -> {
                    fp.addFetchPlan(FetchPlan.BASE);
                    fp.add("categories", FetchPlan.BASE);
                })
                .optional()
                .orElse(contacte);

        List<String> catsEntitat = new ArrayList<>();
        List<String> catsContacte = new ArrayList<>();

        for (Carrec carrec : totsElsCarrecs) {
            if (carrec.getEntitat() != null && carrec.getEntitat().getCategories() != null) {
                for (Categoria cat : carrec.getEntitat().getCategories()) {
                    if (cat.getNom() != null && !catsEntitat.contains(cat.getNom())) {
                        catsEntitat.add(cat.getNom());
                    }
                }
            }
        }

        if (reloadContacte.getCategories() != null) {
            for (Categoria cat : reloadContacte.getCategories()) {
                if (cat.getNom() != null) {
                    if (!catsEntitat.contains(cat.getNom()) && !catsContacte.contains(cat.getNom())) {
                        catsContacte.add(cat.getNom());
                    }
                }
            }
        }

        List<String> llistaFinalUnificada = new ArrayList<>();
        llistaFinalUnificada.addAll(catsEntitat);
        llistaFinalUnificada.addAll(catsContacte);

        if (llistaFinalUnificada.isEmpty()) {
            return "";
        }

        return llistaFinalUnificada.stream()
                .collect(Collectors.joining(", "));
    }

    /**
     * LÓGICA DE VISTA DE DETALLE:
     * Procesa y cruza todos los datos necesarios para la vista de detalle de un Contacto.
     */
    public DetallCampsProcesats procesarDadesDetall(Contacte contacte) {
        if (contacte == null) {
            return new DetallCampsProcesats("", "", "");
        }

        List<Carrec> totsElsCarrecs = dataManager.load(Carrec.class)
                .query("select c from Carrec c where c.contacte = :contacte")
                .parameter("contacte", contacte)
                .fetchPlan(fp -> {
                    fp.addFetchPlan(FetchPlan.BASE);
                    fp.add("entitat", FetchPlan.BASE);
                    fp.add("entitat.categories", FetchPlan.BASE);
                })
                .list();

        if (totsElsCarrecs.isEmpty()) {
            return new DetallCampsProcesats("", "", "");
        }

        // 1. Mapeamos entidades asociadas
        String nomEntitats = totsElsCarrecs.stream()
                .filter(c -> c.getEntitat() != null)
                .map(c -> c.getEntitat().getNom())
                .distinct()
                .collect(Collectors.joining(", "));

        // 2. Mapeamos títulos (sin distinct para permitir duplicados de nombre de cargo)
        String titolsCarrecs = totsElsCarrecs.stream()
                .map(c -> c.getTitolCarrec() != null ? c.getTitolCarrec() : "x")
                .collect(Collectors.joining(", "));

        // 3. Recolectamos categorías de las organizaciones
        List<Categoria> categoriesDeLesEntitats = totsElsCarrecs.stream()
                .filter(c -> c.getEntitat() != null && c.getEntitat().getCategories() != null)
                .flatMap(c -> c.getEntitat().getCategories().stream())
                .distinct()
                .collect(Collectors.toList());

        String nomsCategories = "Sense categories";
        if (!categoriesDeLesEntitats.isEmpty()) {
            nomsCategories = categoriesDeLesEntitats.stream()
                    .map(Categoria::getNom)
                    .collect(Collectors.joining(", "));

            // Inyección automática en la propia entidad en memoria
            if (contacte.getCategories() == null) {
                contacte.setCategories(new ArrayList<>());
            }
            for (Categoria cat : categoriesDeLesEntitats) {
                if (!contacte.getCategories().contains(cat)) {
                    contacte.getCategories().add(cat);
                }
            }
        }

        return new DetallCampsProcesats(nomEntitats, titolsCarrecs, nomsCategories);
    }
}