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

    @Supply(to = "carrecsDataGrid.nomCompletContacte", subject = "renderer")
    private Renderer<Carrec> nomCompletContacteRenderer() {
        return new TextRenderer<>(c -> carrecService.formatarNomComplet(c.getContacte()));
    }

    // Afegim els renderers per als booleans utilitzant els mètodes de l'entitat
    @Supply(to = "carrecsDataGrid.vigent", subject = "renderer")
    private Renderer<Carrec> vigentRenderer() {
        return new TextRenderer<>(Carrec::getVigentSiNo);
    }

    @Supply(to = "carrecsDataGrid.puntTrameses", subject = "renderer")
    private Renderer<Carrec> puntTramesesRenderer() {
        return new TextRenderer<>(Carrec::getPuntTramesesSiNo);
    }
}