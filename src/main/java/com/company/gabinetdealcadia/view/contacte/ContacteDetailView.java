package com.company.gabinetdealcadia.view.contacte;

import com.company.gabinetdealcadia.entity.Contacte;
import com.company.gabinetdealcadia.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "contactes/:id", layout = MainView.class)
@ViewController(id = "Contacte.detail")
@ViewDescriptor(path = "contacte-detail-view.xml")
@EditedEntityContainer("contacteDc")
public class ContacteDetailView extends StandardDetailView<Contacte> {
}