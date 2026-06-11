package com.company.gabinetdealcadia.view.contacte;

import com.company.gabinetdealcadia.entity.Contacte;
import com.company.gabinetdealcadia.service.CarrecService;
import com.company.gabinetdealcadia.view.main.MainView;
import com.vaadin.flow.router.Route;
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
    private CarrecService carrecService;

    // 1. Renderer per al Nom Complet del Contacte
    @Supply(to = "contactesDataGrid.nomCompletContacte", subject = "renderer")
    private Renderer<Contacte> nomCompletContacteRenderer() {
        return new TextRenderer<>(c -> carrecService.formatarNomComplet(c));
    }

    // 2. NOU: Renderer per a l'Entitat obtinguda a través del Càrrec vigent
    @Supply(to = "contactesDataGrid.entitatPertanyent", subject = "renderer")
    private Renderer<Contacte> entitatPertanyentRenderer() {
        return new TextRenderer<>(contacte -> {
            // Cridem al servei per obtenir el nom de l'entitat vigent d'aquest contacte
            String nomEntitat = carrecService.obtenirNomEntitatVigent(contacte);
            return nomEntitat != null ? nomEntitat : "";
        });
    }
}