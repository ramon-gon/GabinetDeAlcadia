package com.company.gabinetdealcadia.view.carrec;

import com.company.gabinetdealcadia.entity.Carrec;
import com.company.gabinetdealcadia.service.CarrecService;
import com.company.gabinetdealcadia.view.main.MainView;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "carrecs", layout = MainView.class)
@ViewController("Carrec.list")
@ViewDescriptor("carrec-list-view.xml")
@LookupComponent("carrecsDataGrid")
public class CarrecListView extends StandardListView<Carrec> {

    @Autowired
    private CarrecService carrecService;

    // Aquest sí que el mantens perquè el "Nom Complet" no és un camp de la BBDD
    @Supply(to = "carrecsDataGrid.nomCompletContacte", subject = "renderer")
    private Renderer<Carrec> nomCompletContacteRenderer() {
        return new TextRenderer<>(c -> carrecService.formatarNomComplet(c.getContacte()));
    }
}