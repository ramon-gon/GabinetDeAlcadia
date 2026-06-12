package com.company.gabinetdealcadia.view.categoria;

import com.company.gabinetdealcadia.entity.Categoria;

import com.company.gabinetdealcadia.view.main.MainView;

import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "categorias", layout = MainView.class)
@ViewController(id = "Categoria.list")
@ViewDescriptor(path = "categoria-list-view.xml")
@LookupComponent("categoriasDataGrid")
@DialogMode(width = "64em")
public class CategoriaListView extends StandardListView<Categoria> {
}