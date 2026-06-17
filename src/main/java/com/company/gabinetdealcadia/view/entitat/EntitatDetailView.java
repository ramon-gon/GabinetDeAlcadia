package com.company.gabinetdealcadia.view.entitat;

import com.company.gabinetdealcadia.entity.Entitat;
import com.company.gabinetdealcadia.entity.Categoria;
import com.company.gabinetdealcadia.view.main.MainView;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.core.FetchPlan;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Route(value = "entitats/:id", layout = MainView.class)
@ViewController("Entitat.detail")
@ViewDescriptor("entitat-detail-view.xml")
@EditedEntityContainer("entitatDc")
public class EntitatDetailView extends StandardDetailView<Entitat> {

    // 1. Cambiamos el componente a MultiSelectComboBox para soportar las múltiples categorías
    @ViewComponent
    private MultiSelectComboBox<Categoria> categoriesField;

    @Autowired
    private DataManager dataManager;

    @Subscribe
    public void onInit(final InitEvent event) {
        // 2. Cargamos todas las categorías reales de la base de datos de forma segura
        List<Categoria> totesLesCategories = dataManager.load(Categoria.class)
                .all()
                .fetchPlan(FetchPlan.BASE)
                .list();

        // 3. Asignamos las categorías disponibles al componente múltiple
        categoriesField.setItems(totesLesCategories);

        // Usamos el campo 'nom' de la categoría para mostrarlo en los chips visuales
        categoriesField.setItemLabelGenerator(Categoria::getNom);
    }

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        // 4. Al abrir la pantalla, cargamos las categorías que ya tiene guardadas la Entidad
        Entitat entitat = getEditedEntity();
        if (entitat.getCategories() != null) {
            categoriesField.setValue(new HashSet<>(entitat.getCategories()));
        }
    }

    @Subscribe
    public void onBeforeSave(final BeforeSaveEvent event) {
        // 5. Antes de guardar, volcamos la selección del MultiSelectComboBox a la entidad
        Entitat entitat = getEditedEntity();

        if (entitat.getCategories() == null) {
            entitat.setCategories(new ArrayList<>());
        } else {
            entitat.getCategories().clear();
        }

        // Sincronizamos la colección interna de Jmix
        entitat.getCategories().addAll(categoriesField.getValue());
    }
}