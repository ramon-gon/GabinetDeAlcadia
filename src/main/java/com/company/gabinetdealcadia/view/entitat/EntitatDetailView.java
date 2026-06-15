package com.company.gabinetdealcadia.view.entitat;

import com.company.gabinetdealcadia.entity.Entitat;
import com.company.gabinetdealcadia.entity.Categoria;
import com.company.gabinetdealcadia.view.main.MainView;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

@Route(value = "entitats/:id", layout = MainView.class)
@ViewController("Entitat.detail")
@ViewDescriptor("entitat-detail-view.xml")
@EditedEntityContainer("entitatDc")
public class EntitatDetailView extends StandardDetailView<Entitat> {

    @ViewComponent
    private MultiSelectComboBox<Categoria> categoriesField; // Ara apunta a l'entitat Categoria

    @Autowired
    private DataManager dataManager;

    @Subscribe
    public void onInit(final InitEvent event) {
        // Carreguem totes les categories disponibles de la base de dades per al desplegable
        categoriesField.setItems(dataManager.load(Categoria.class).all().list());
        categoriesField.setItemLabelGenerator(Categoria::getNom);
    }
}
