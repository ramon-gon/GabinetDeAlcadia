package com.company.gabinetdealcadia.view.entitat;

import com.company.gabinetdealcadia.entity.Entitat;
import com.company.gabinetdealcadia.view.main.MainView;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Route(value = "entitats/:id", layout = MainView.class)
@ViewController("Entitat.detail")
@ViewDescriptor("entitat-detail-view.xml")
@EditedEntityContainer("entitatDc")
public class EntitatDetailView extends StandardDetailView<Entitat> {

    @ViewComponent
    private ComboBox<String> categoriaField;

    @Autowired
    private DataManager dataManager;

    @Subscribe
    public void onInit(final InitEvent event) {
        // 1. 🛠️ CORREGIT: Utilitzem loadValues() en lloc de load(Entitat.class)
        // i mapegem la query com un String per recuperar la llista de textos neta.
        List<String> categoriesExistents = dataManager.loadValues(
                        "select distinct e.categoria from Entitat e where e.categoria is not null order by e.categoria"
                )
                .properties("categoria") // Definim la propietat que extreiem de la query
                .list()
                .stream()
                .map(keyValueEntity -> keyValueEntity.<String>getValue("categoria")) // Extreiem el text de la cel·la
                .toList();

        // 2. Assignem els ítems de base al combo de Vaadin
        categoriaField.setItems(categoriesExistents);
        categoriaField.setAllowCustomValue(true);

        // 3. Captura d'un valor completament nou escrit a mà
        categoriaField.addCustomValueSetListener(e -> {
            String valorNou = e.getDetail();

            if (valorNou != null && !valorNou.trim().isEmpty()) {
                String netejat = valorNou.trim();

                List<String> llistaActualitzada = new ArrayList<>(categoriesExistents);
                if (!llistaActualitzada.contains(netejat)) {
                    llistaActualitzada.add(netejat);
                    categoriaField.setItems(llistaActualitzada);
                }

                getEditedEntity().setCategoria(netejat);
                categoriaField.setValue(netejat);
            }
        });

        // 4. Si l'usuari en tria una de la llista desplegable
        categoriaField.addValueChangeListener(e -> {
            String valorTriat = e.getValue();
            getEditedEntity().setCategoria(valorTriat != null ? valorTriat.trim() : null);
        });
    }
    // 🛠️ PAS 2: Sincronització quan l'entitat es carrega a la pantalla (Mode Edició)
    @Subscribe(id = "entitatDc", target = Target.DATA_CONTAINER)
    public void onEntitatDcItemChange(final InstanceContainer.ItemChangeEvent<Entitat> event) {
        Entitat entitat = event.getItem();
        if (entitat != null && entitat.getCategoria() != null) {
            String catActual = entitat.getCategoria();

            // Assegurem que el valor actual de l'entitat estigui inclòs dins els ítems
            // per si obrim un registre que té una categoria que s'havia escrit manualment
            categoriaField.setValue(catActual);
        } else {
            categoriaField.setValue(null);
        }
    }
}