package com.company.gabinetdealcadia.view.carrec;

import com.company.gabinetdealcadia.entity.Carrec;
import com.company.gabinetdealcadia.entity.Contacte;
import com.company.gabinetdealcadia.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;

@Route(value = "carrecs", layout = MainView.class)
@ViewController("Carrec.list")
@ViewDescriptor("carrec-list-view.xml")
@LookupComponent("carrecsDataGrid")
public class CarrecListView extends StandardListView<Carrec> {

    @Supply(to = "carrecsDataGrid.nomCompletContacte", subject = "renderer")
    private com.vaadin.flow.data.renderer.Renderer<Carrec> nomCompletContacteRenderer() {
        return new com.vaadin.flow.data.renderer.TextRenderer<>(carrec -> {
            Contacte contacte = carrec.getContacte();
            if (contacte == null) {
                return "";
            }
            String nom = contacte.getNom() != null ? contacte.getNom() : "";
            String primerCognom = contacte.getPrimer_cognom() != null ? contacte.getPrimer_cognom() : "";
            String segonCognom = contacte.getSegon_cognom() != null ? contacte.getSegon_cognom() : "";

            return String.format("%s %s %s", nom, primerCognom, segonCognom).trim().replaceAll("\\s+", " ");
        });
    }
}