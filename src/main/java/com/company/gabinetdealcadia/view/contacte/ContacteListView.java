package com.company.gabinetdealcadia.view.contacte;

import com.company.gabinetdealcadia.entity.Contacte;
import com.company.gabinetdealcadia.service.CarrecService;
import com.company.gabinetdealcadia.service.ContacteService; // 🛠️ Tu nuevo servicio de entidad
import com.company.gabinetdealcadia.service.DataGridService; // 🛠️ Tu servicio de UI
import com.company.gabinetdealcadia.view.main.MainView;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.view.*;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "contactes", layout = MainView.class)
@ViewController("Contacte.list")
@ViewDescriptor("contacte-list-view.xml")
@LookupComponent("contactesDataGrid")
@DialogMode(width = "64em")
public class ContacteListView extends StandardListView<Contacte> {

    @Autowired
    private DataGridService dataGridService; // Para la UI (Doble clic)

    @Autowired
    private ContacteService contacteService; // Para el negocio de Contactos

    @Autowired
    private CarrecService carrecService; // Para el negocio de Cargos

    @ViewComponent
    private DataGrid<Contacte> contactesDataGrid;

    // 1. Renderer - Nombre completo (Usa CarrecService)
    @Supply(to = "contactesDataGrid.nomcompletcontacte", subject = "renderer")
    private Renderer<Contacte> nomCompletContacteRenderer() {
        return new TextRenderer<>(c -> carrecService.formatarNomComplet(c));
    }

    // 2. Renderer - Entidades pertenecientes (Usa ContacteService)
    @Supply(to = "contactesDataGrid.entitatpertanyent", subject = "renderer")
    private Renderer<Contacte> entitatPertanyentRenderer() {
        return new TextRenderer<>(contacte -> contacteService.obtenerEntitatsPertanyents(contacte));
    }

    // 3. Renderer - Categorías unificadas (Usa ContacteService)
    @Supply(to = "contactesDataGrid.categoriesColumn", subject = "renderer")
    private Renderer<Contacte> categoriesColumnRenderer() {
        return new TextRenderer<>(contacte -> contacteService.obtenerCategoriesUnificades(contacte));
    }

    // Evento de Doble Clic (Usa DataGridService)
    @Subscribe("contactesDataGrid")
    public void onContactesDataGridItemDoubleClick(final ItemClickEvent<Contacte> event) {
        dataGridService.handleItemDoubleClick(event, contactesDataGrid);
    }
}