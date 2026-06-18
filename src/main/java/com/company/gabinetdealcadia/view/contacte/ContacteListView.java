package com.company.gabinetdealcadia.view.contacte;

import com.company.gabinetdealcadia.entity.Contacte;
import com.company.gabinetdealcadia.entity.Carrec;
import com.company.gabinetdealcadia.entity.Categoria;
import com.company.gabinetdealcadia.service.CarrecService;
import com.company.gabinetdealcadia.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.core.FetchPlan;
import io.jmix.flowui.view.*;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Route(value = "contactes", layout = MainView.class)
@ViewController("Contacte.list")
@ViewDescriptor("contacte-list-view.xml")
@LookupComponent("contactesDataGrid")
@DialogMode(width = "64em")
public class ContacteListView extends StandardListView<Contacte> {

    @Autowired
    private CarrecService carrecService;

    @Autowired
    private DataManager dataManager; // Gestor de datos seguro de Jmix

    // 1. Renderer per al nom complet del contacte
    @Supply(to = "contactesDataGrid.nomcompletcontacte", subject = "renderer")
    private Renderer<Contacte> nomCompletContacteRenderer() {
        return new TextRenderer<>(c -> carrecService.formatarNomComplet(c));
    }

    // 2. Renderer per a les entitats associades als càrrecs del contacte
    @Supply(to = "contactesDataGrid.entitatpertanyent", subject = "renderer")
    private Renderer<Contacte> entitatPertanyentRenderer() {
        return new TextRenderer<>(contacte -> {
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
        });
    }

    // 3. 🌟 RENDERER OPTIMIZADO: Primero categorías de Entidad (Bat) y luego las del Contacto
    @Supply(to = "contactesDataGrid.categoriesColumn", subject = "renderer")
    private Renderer<Contacte> categoriesColumnRenderer() {
        return new TextRenderer<>(contacte -> {

            // A) Buscamos todos los cargos y cargamos las categorías de sus entidades vinculadas (Bat)
            List<Carrec> totsElsCarrecs = dataManager.load(Carrec.class)
                    .query("select c from Carrec c where c.contacte = :contacte")
                    .parameter("contacte", contacte)
                    .fetchPlan(fp -> {
                        fp.addFetchPlan(FetchPlan.BASE);
                        fp.add("entitat", FetchPlan.BASE);
                        fp.add("entitat.categories", FetchPlan.BASE);
                    })
                    .list();

            // B) Forzamos la recarga del contacto para traer sus categorías independientes de la ficha
            Contacte reloadContacte = dataManager.load(Contacte.class)
                    .id(contacte.getId())
                    .fetchPlan(fp -> {
                        fp.addFetchPlan(FetchPlan.BASE);
                        fp.add("categories", FetchPlan.BASE);
                    })
                    .optional()
                    .orElse(contacte);

            // Listas separadas para controlar los dos bloques y evitar duplicaciones globales
            List<String> catsEntitat = new ArrayList<>();
            List<String> catsContacte = new ArrayList<>();

            // 1. Primero recolectamos las categorías de las Entidades (Bat)
            for (Carrec carrec : totsElsCarrecs) {
                if (carrec.getEntitat() != null && carrec.getEntitat().getCategories() != null) {
                    for (Categoria cat : carrec.getEntitat().getCategories()) {
                        if (cat.getNom() != null && !catsEntitat.contains(cat.getNom())) {
                            catsEntitat.add(cat.getNom());
                        }
                    }
                }
            }

            // 2. Después recolectamos las categorías independientes del Contacto
            if (reloadContacte.getCategories() != null) {
                for (Categoria cat : reloadContacte.getCategories()) {
                    if (cat.getNom() != null) {
                        // Solo la añadimos si no ha salido ya en el bloque de entidades para no repetir texto
                        if (!catsEntitat.contains(cat.getNom()) && !catsContacte.contains(cat.getNom())) {
                            catsContacte.add(cat.getNom());
                        }
                    }
                }
            }

            // Unimos los dos bloques manteniendo estrictamente el orden: Primero Entidad, luego Contacto
            List<String> llistaFinalUnificada = new ArrayList<>();
            llistaFinalUnificada.addAll(catsEntitat);
            llistaFinalUnificada.addAll(catsContacte);

            if (llistaFinalUnificada.isEmpty()) {
                return "";
            }

            // Concatenamos todo el resultado final con comas limpiamente
            return llistaFinalUnificada.stream()
                    .collect(Collectors.joining(", "));
        });
    }
}