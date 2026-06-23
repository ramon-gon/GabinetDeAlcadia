package com.company.gabinetdealcadia.view.entitat;

import com.company.gabinetdealcadia.entity.Entitat;
import com.company.gabinetdealcadia.service.EntitatService;
import com.company.gabinetdealcadia.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.view.*;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

@Route(value = "entitats", layout = MainView.class)
@ViewController("Entitat.list")
@ViewDescriptor("entitat-list-view.xml")
@LookupComponent("entitatsDataGrid")
@DialogMode(width = "64em")
public class EntitatListView extends StandardListView<Entitat> {

    @Autowired
    private EntitatService entitatService;

    @ViewComponent
    private DataGrid<Entitat> entitatsDataGrid;

    /**
     * 🛠️ Renderitzador per a la columna calculada del president.
     */
    @Supply(to = "entitatsDataGrid.presidentColumn", subject = "renderer")
    private Renderer<Entitat> presidentColumnRenderer() {
        return new TextRenderer<>(entitat -> entitatService.obtenirNomPresident(entitat));
    }

    /**
     * 🌟 OPTIMIZADO: Saca las categorías directamente de la memoria RAM del FetchPlan
     * sin ejecutar subconsultas SQL adicionales por cada fila.
     */
    @Supply(to = "entitatsDataGrid.categoriesColumn", subject = "renderer")
    private Renderer<Entitat> categoriesColumnRenderer() {
        return new TextRenderer<>(entitat -> {
            if (entitat.getCategories() == null || entitat.getCategories().isEmpty()) {
                return "-";
            }
            return entitat.getCategories().stream()
                    .map(cat -> cat.getNom())
                    .filter(nom -> nom != null && !nom.isBlank())
                    .collect(Collectors.joining(", "));
        });
    }
}