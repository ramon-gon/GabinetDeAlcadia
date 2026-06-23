package com.company.gabinetdealcadia.view.contacte;

import com.company.gabinetdealcadia.entity.Contacte;
import com.company.gabinetdealcadia.service.CarrecService;
import com.company.gabinetdealcadia.service.ContacteService;
import com.company.gabinetdealcadia.service.DataGridService;
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
    private DataGridService dataGridService;

    @Autowired
    private ContacteService contacteService;

    @Autowired
    private CarrecService carrecService;

    @ViewComponent
    private DataGrid<Contacte> contactesDataGrid;

    // 1. Renderer - Nombre completo
    @Supply(to = "contactesDataGrid.nomcompletcontacte", subject = "renderer")
    private Renderer<Contacte> nomCompletContacteRenderer() {
        return new TextRenderer<>(c -> carrecService.formatarNomComplet(c));
    }

    // 2. Renderer - Entidades pertenecientes
    @Supply(to = "contactesDataGrid.entitatpertanyent", subject = "renderer")
    private Renderer<Contacte> entitatPertanyentRenderer() {
        return new TextRenderer<>(contacte -> contacteService.obtenerEntitatsPertanyents(contacte));
    }

    // 3. Renderer - Categorías unificadas
    @Supply(to = "contactesDataGrid.categoriesColumn", subject = "renderer")
    private Renderer<Contacte> categoriesColumnRenderer() {
        return new TextRenderer<>(contacte -> contacteService.obtenerCategoriesUnificades(contacte));
    }

    // 4. Renderer - Teléfonos
    @Supply(to = "contactesDataGrid.telefonMobil", subject = "renderer")
    private Renderer<Contacte> telefonMobilRenderer() {
        return new TextRenderer<>(contacte -> {
            if (contacte.getTelefonMobil() == null || contacte.getTelefonMobil().isBlank()) {
                return "";
            }
            return contacte.getTelefonMobil();
        });
    }

    // 5. Renderer - Comentarios
    @Supply(to = "contactesDataGrid.comentariTelefonMobil", subject = "renderer")
    private Renderer<Contacte> comentariTelefonMobilRenderer() {
        return new TextRenderer<>(contacte -> {
            if (contacte.getComentariTelefonMobil() == null || contacte.getComentariTelefonMobil().isBlank()) {
                return "";
            }
            return contacte.getComentariTelefonMobil();
        });
    }

    // Evento de Doble Clic
    @Subscribe("contactesDataGrid")
    public void onContactesDataGridItemDoubleClick(final ItemClickEvent<Contacte> event) {
        dataGridService.handleItemDoubleClick(event, contactesDataGrid);
    }
}