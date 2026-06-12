package com.company.gabinetdealcadia.view.categoria;

import com.company.gabinetdealcadia.entity.Categoria;
import com.company.gabinetdealcadia.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "categorias/:id", layout = MainView.class)
@ViewController(id = "Categoria.detail")
@ViewDescriptor(path = "categoria-detail-view.xml")
@EditedEntityContainer("categoriaDc")
public class CategoriaDetailView extends StandardDetailView<Categoria> {
}