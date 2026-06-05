
package com.company.gabinetdealcadia.view.entitat;

import com.company.gabinetdealcadia.entity.Entitat;

import com.company.gabinetdealcadia.view.main.MainView;

import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "entitats", layout = MainView.class)
@ViewController(id = "Entitat.list")
@ViewDescriptor(path = "entitat-list-view.xml")
@LookupComponent("entitatsDataGrid")
@DialogMode(width = "64em")
    public class EntitatListView extends StandardListView<Entitat> {
    @Supply(to = "entitatsDataGrid.activa", subject = "renderer")
    private Renderer<Entitat> activaRenderer() {
        // Assumeix que has afegit el mètode getActivaSiNo() a Entitat.java
        return new TextRenderer<>(Entitat::getActivaSiNo);
    }
    }