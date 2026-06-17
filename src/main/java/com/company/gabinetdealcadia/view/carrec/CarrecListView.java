package com.company.gabinetdealcadia.view.carrec;

import com.company.gabinetdealcadia.entity.Carrec;

import com.company.gabinetdealcadia.view.main.MainView;

import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "carrecs", layout = MainView.class)
@ViewController(id = "Carrec.list")
@ViewDescriptor(path = "carrec-list-view.xml")
@LookupComponent("carrecsDataGrid")
@DialogMode(width = "64em")
public class CarrecListView extends StandardListView<Carrec> {

@Supply(to = "carrecsDataGrid.vigent", subject = "renderer")
private Renderer<Carrec> vigentRenderer() {return new TextRenderer<>(Carrec::getVigentSiNo);
}

@Supply(to = "carrecsDataGrid.puntTrameses", subject = "renderer")
private Renderer<Carrec> puntTramesesRenderer() {return new TextRenderer<>(Carrec::getPuntTramesesSiNo);
}
}