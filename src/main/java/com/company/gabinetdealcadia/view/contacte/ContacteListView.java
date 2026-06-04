package com.company.gabinetdealcadia.view.contacte;

import com.company.gabinetdealcadia.entity.Contacte;
import com.company.gabinetdealcadia.service.CarrecService;
import com.company.gabinetdealcadia.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;
import io.jmix.flowui.component.grid.DataGrid;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "contactes", layout = MainView.class)
@ViewController("Contacte.list")
@ViewDescriptor("contacte-list-view.xml")
public class ContacteListView extends StandardListView<Contacte> {

    @Autowired
    private CarrecService carrecService; // Reutilitza el teu servei o crea ContacteService

    // Només deixem els renderers per a columnes "virtuals" (les que no són camps directes)
    @Supply(to = "contactesDataGrid.nomCompletContacte", subject = "renderer")
    private Renderer<Contacte> nomCompletContacteRenderer() {
        return new TextRenderer<>(c -> carrecService.formatarNomComplet(c));
    }

}