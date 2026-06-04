package com.company.gabinetdealcadia.view.entitat;

import com.company.gabinetdealcadia.entity.Entitat;
import com.company.gabinetdealcadia.service.EntitatService;
import com.company.gabinetdealcadia.view.main.MainView;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "entitats/:id", layout = MainView.class)
@ViewController("Entitat.detail")
@ViewDescriptor("entitat-detail-view.xml")
@EditedEntityContainer("entitatDc")
public class EntitatDetailView extends StandardDetailView<Entitat> {

    @ViewComponent
    private ComboBox<String> categoriaField;

    @Autowired
    private EntitatService entitatService; // Injectem el servei

    @Subscribe
    public void onInit(final InitEvent event) {
        // Obtenim la llista a través del servei
        List<String> categories = entitatService.obtenirCategoriesUniques();

        categoriaField.setItems(categories);
        categoriaField.setAllowCustomValue(true);

        categoriaField.addCustomValueSetListener(e -> {
            String valor = e.getDetail();
            if (valor != null && !valor.isBlank()) {
                String netejat = valor.trim();
                getEditedEntity().setCategoria(netejat);
                categoriaField.setValue(netejat);
            }
        });
    }
}