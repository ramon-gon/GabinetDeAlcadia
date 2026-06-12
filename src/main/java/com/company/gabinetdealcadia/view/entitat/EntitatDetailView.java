package com.company.gabinetdealcadia.view.entitat;

import com.company.gabinetdealcadia.entity.Categoria;
import com.company.gabinetdealcadia.entity.Entitat;
import com.company.gabinetdealcadia.view.main.MainView;
import com.vaadin.flow.component.combobox.MultiSelectComboBox; // ◄ Import estándar de Vaadin
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Route(value = "entitats/:id", layout = MainView.class)
@ViewController("Entitat.detail")
@ViewDescriptor("entitat-detail-view.xml")
@EditedEntityContainer("entitatDc")
public class EntitatDetailView extends StandardDetailView<Entitat> {

    // ◄ CAMBIO: Cambiado el tipo al combobox múltiple estándar
    @ViewComponent
    private MultiSelectComboBox<Categoria> categoriasField;

    @Autowired
    private DataManager dataManager;

    @Subscribe
    public void onInit(final InitEvent event) {
        // Permitimos escribir valores personalizados que no estén en la lista
        categoriasField.setAllowCustomValue(true);

        // Capturamos el evento cuando se escribe una categoría nueva y se pulsa Enter
        categoriasField.addCustomValueSetListener(e -> {
            String nouValor = e.getDetail();
            if (nouValor == null || nouValor.isBlank()) {
                return;
            }
            String netejat = nouValor.trim();

            // Buscamos si ya existe en la base de datos
            Categoria categoriaExistents = dataManager.load(Categoria.class)
                    .query("select c from Categoria c where lower(c.nom) = lower(:nom)")
                    .parameter("nom", netejat)
                    .optional()
                    .orElse(null);

            // Si no existe, la creamos y la guardamos
            if (categoriaExistents == null) {
                categoriaExistents = dataManager.create(Categoria.class);
                categoriaExistents.setNom(netejat);
                categoriaExistents = dataManager.save(categoriaExistents);
            }

            // Añadimos la categoría a la lista de la Entidad actual
            Entitat entitat = getEditedEntity();
            if (entitat.getCategorias() == null) {
                entitat.setCategorias(new ArrayList<>());
            }

            if (!entitat.getCategorias().contains(categoriaExistents)) {
                entitat.getCategorias().add(categoriaExistents);

                // Actualizamos visualmente el componente con los elementos seleccionados (usa un Set)
                Set<Categoria> valoresActuales = new HashSet<>(categoriasField.getValue());
                valoresActuales.add(categoriaExistents);
                categoriasField.setValue(valoresActuales);
            }
        });
    }
}