package com.company.gabinetdealcadia.view.entitat;

import com.company.gabinetdealcadia.entity.Entitat;
import com.company.gabinetdealcadia.entity.Categoria;
import com.company.gabinetdealcadia.service.EntitatService; // 🛠️ Inyectamos su servicio de negocio dedicado
import com.company.gabinetdealcadia.view.main.MainView;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;

@Route(value = "entitats/:id", layout = MainView.class)
@ViewController("Entitat.detail")
@ViewDescriptor("entitat-detail-view.xml")
@EditedEntityContainer("entitatDc")
public class EntitatDetailView extends StandardDetailView<Entitat> {

    @Autowired
    private EntitatService entitatService; // 🛠️ Único punto de contacto para datos

    @ViewComponent
    private MultiSelectComboBox<Categoria> categoriesField;

    @Subscribe
    public void onInit(final InitEvent event) {
        // 1. Poblamos los items del combo delegando en el servicio
        categoriesField.setItems(entitatService.cargarTodasLasCategorias());
        categoriesField.setItemLabelGenerator(Categoria::getNom);
    }

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        // 2. Cargamos las categorías actuales de la entidad en el campo visual
        Entitat entitat = getEditedEntity();
        if (entitat.getCategories() != null) {
            categoriesField.setValue(new HashSet<>(entitat.getCategories()));
        }
    }

    @Subscribe
    public void onBeforeSave(final BeforeSaveEvent event) {
        // 3. Pasamos la entidad y el valor del combo al servicio para que resuelva la persistencia
        entitatService.sincronizarCategorias(getEditedEntity(), categoriesField.getValue());
    }
}