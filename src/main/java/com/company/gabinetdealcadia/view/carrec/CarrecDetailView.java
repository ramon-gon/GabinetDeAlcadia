package com.company.gabinetdealcadia.view.carrec;

import com.company.gabinetdealcadia.entity.Carrec;
import com.company.gabinetdealcadia.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "carrecs/:id", layout = MainView.class)
@ViewController(id = "Carrec.detail")
@ViewDescriptor(path = "carrec-detail-view.xml")
@EditedEntityContainer("carrecDc")
public class CarrecDetailView extends StandardDetailView<Carrec> {
}