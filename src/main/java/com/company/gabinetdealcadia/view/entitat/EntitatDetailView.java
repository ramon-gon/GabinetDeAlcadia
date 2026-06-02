package com.company.gabinetdealcadia.view.entitat;

import com.company.gabinetdealcadia.entity.Entitat;
import com.company.gabinetdealcadia.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "entitats/:id", layout = MainView.class)
@ViewController(id = "Entitat.detail")
@ViewDescriptor(path = "entitat-detail-view.xml")
@EditedEntityContainer("entitatDc")
public class EntitatDetailView extends StandardDetailView<Entitat> {
}