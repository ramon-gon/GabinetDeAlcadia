package com.company.gabinetdealcadia.view.entitat;

import com.company.gabinetdealcadia.entity.Entitat;
import com.company.gabinetdealcadia.entity.Categoria;
import com.company.gabinetdealcadia.view.main.MainView;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.core.FetchPlan;
import io.jmix.flowui.action.list.ReadAction;
import io.jmix.flowui.component.grid.DataGrid;
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
    private DataManager dataManager;

    // Inyectamos el componente de la tabla de entidades
    @ViewComponent
    private DataGrid<Entitat> entitatsDataGrid;

    // 1. RENDERER PER A LES CATEGORIES
    @Supply(to = "entitatsDataGrid.categoriesColumn", subject = "renderer")
    private Renderer<Entitat> categoriesColumnRenderer() {
        return new TextRenderer<>(entitat -> {
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
        });
    }

    // 2. RENDERER PER AL CAMP ACTIVA
    @Supply(to = "entitatsDataGrid.activa", subject = "renderer")
    private Renderer<Entitat> activaRenderer() {
        return new TextRenderer<>(Entitat::getActivaSiNo);
    }

    // NUEVO: Escuchamos el evento de doble clic de forma segura
    @Subscribe("entitatsDataGrid")
    public void onEntitatsDataGridItemDoubleClick(final ItemClickEvent<Entitat> event) {
        if (event.getClickCount() == 2 && event.getItem() != null) {
            // Forzamos la selección de la entidad para que ReadAction sepa qué abrir
            entitatsDataGrid.select(event.getItem());

            // Ejecutamos la acción de lectura
            ReadAction<Entitat> readAction = (ReadAction<Entitat>) entitatsDataGrid.getAction("readAction");
            if (readAction != null) {
                readAction.execute();
            }
        }
    }
}