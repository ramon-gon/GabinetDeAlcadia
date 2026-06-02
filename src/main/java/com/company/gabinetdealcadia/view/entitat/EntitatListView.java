package com.company.gabinetdealcadia.view.entitat;

import com.company.gabinetdealcadia.entity.Entitat;

import com.company.gabinetdealcadia.view.main.MainView;

import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "entitats", layout = MainView.class)
@ViewController(id = "Entitat.list")
@ViewDescriptor(path = "entitat-list-view.xml")
@LookupComponent("entitatsDataGrid")
@DialogMode(width = "64em")
    public class EntitatListView extends StandardListView<Entitat> {
    }