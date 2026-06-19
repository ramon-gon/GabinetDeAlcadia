package com.company.gabinetdealcadia.view.entitat;

import com.company.gabinetdealcadia.entity.Entitat;
import com.company.gabinetdealcadia.service.DataGridService; // 🛠️ Servicio de interfaz genérico
import com.company.gabinetdealcadia.service.EntitatService;   // 🛠️ Servicio de negocio específico
import com.company.gabinetdealcadia.view.main.MainView;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "entitats", layout = MainView.class)
@ViewController(id = "Entitat.list")
@ViewDescriptor(path = "entitat-list-view.xml")
@LookupComponent("entitatsDataGrid")
@DialogMode(width = "64em")
public class EntitatListView extends StandardListView<Entitat> {

    @Autowired
    private DataGridService dataGridService; // Lógica visual unificada

    @Autowired
    private EntitatService entitatService;   // Lógica de negocio de Entidades

    @ViewComponent
    private DataGrid<Entitat> entitatsDataGrid;

    // 1. RENDERER PER A LES CATEGORIES (Delegado al servicio de entidad)
    @Supply(to = "entitatsDataGrid.categoriesColumn", subject = "renderer")
    private Renderer<Entitat> categoriesColumnRenderer() {
        return new TextRenderer<>(entitat -> entitatService.obtenerNomsCategories(entitat));
    }

    // 2. RENDERER PER AL CAMP ACTIVA (Llamada limpia directa al método de la entidad)
    @Supply(to = "entitatsDataGrid.activa", subject = "renderer")
    private Renderer<Entitat> activaRenderer() {
        return new TextRenderer<>(Entitat::getActivaSiNo);
    }

    // EVENTO DOBLE CLIC (Delegado al servicio genérico de UI)
    @Subscribe("entitatsDataGrid")
    public void onEntitatsDataGridItemDoubleClick(final ItemClickEvent<Entitat> event) {
        dataGridService.handleItemDoubleClick(event, entitatsDataGrid);
    }
}