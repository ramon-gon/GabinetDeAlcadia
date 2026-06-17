package com.company.gabinetdealcadia.view.entitat;

import com.company.gabinetdealcadia.entity.Entitat;
import com.company.gabinetdealcadia.entity.Categoria;
import com.company.gabinetdealcadia.view.main.MainView;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.core.FetchPlan;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.stream.Collectors;

@Route(value = "entitats", layout = MainView.class)
@ViewController(id = "Entitat.list")
@ViewDescriptor(path = "entitat-list-view.xml")
@LookupComponent("entitatsDataGrid")
@DialogMode(width = "64em")
public class EntitatListView extends StandardListView<Entitat> {

    @Autowired
    private DataManager dataManager; // 🛠️ Injectem el gestor de dades de Jmix per evitar el Lazy Loading

    // 1. 🛠️ RENDERER PER A LES CATEGORIES (Afegeix això perquè es puguin veure!)
    @Supply(to = "entitatsDataGrid.categoriesColumn", subject = "renderer")
    private Renderer<Entitat> categoriesColumnRenderer() {
        return new TextRenderer<>(entitat -> {
            // Forcem a Jmix a carregar la col·lecció 'categories' de la base de dades si ve buida (Lazy)
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

            // Concatenem els noms de les categories trobades separades per una coma i un espai
            return reloadEntitat.getCategories().stream()
                    .map(Categoria::getNom)
                    .collect(Collectors.joining(", "));
        });
    }

    // 2. RENDERER PER AL CAMP ACTIVA (El teu codi original de Sí/No)
    @Supply(to = "entitatsDataGrid.activa", subject = "renderer")
    private Renderer<Entitat> activaRenderer() {
        return new TextRenderer<>(Entitat::getActivaSiNo);
    }
}