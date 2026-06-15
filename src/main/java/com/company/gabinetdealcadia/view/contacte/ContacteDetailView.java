package com.company.gabinetdealcadia.view.contacte;

import com.company.gabinetdealcadia.entity.Contacte;
import com.company.gabinetdealcadia.entity.Carrec;
import com.company.gabinetdealcadia.entity.Entitat;
import com.company.gabinetdealcadia.entity.Categoria;
import com.company.gabinetdealcadia.view.main.MainView;
import com.company.gabinetdealcadia.service.CarrecService;
import com.vaadin.flow.component.textfield.TextField;
import io.jmix.core.DataManager;
import io.jmix.core.FetchPlan;
import io.jmix.flowui.view.*;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Route(value = "contactes/:id", layout = MainView.class)
@ViewController("Contacte.detail")
@ViewDescriptor("contacte-detail-view.xml")
@EditedEntityContainer("contacteDc")
public class ContacteDetailView extends StandardDetailView<Contacte> {

    @Autowired
    private CarrecService carrecService;

    @Autowired
    private DataManager dataManager;

    @ViewComponent
    private TextField carrecEntitatNomField;

    @ViewComponent
    private TextField carrecTitolField;

    @ViewComponent
    private TextField entitatCategoriesField;

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        Contacte contacte = getEditedEntity();

        if (contacte != null) {
            Carrec carrecVigent = carrecService.obtenirCarrecVigent(contacte);

            if (carrecVigent != null && carrecVigent.getEntitat() != null) {
                Entitat entitat = carrecVigent.getEntitat();

                // 1. Asignamos el Nombre de la Entidad
                if (entitat.getNom() != null) {
                    carrecEntitatNomField.setValue(entitat.getNom());
                } else {
                    carrecEntitatNomField.setValue("");
                }

                // 2. Asignamos el Título del Cargo
                if (carrecVigent.getTitolCarrec() != null) {
                    carrecTitolField.setValue(carrecVigent.getTitolCarrec());
                } else {
                    carrecTitolField.setValue("x");
                }

                // 3. Cargamos la Entidad asegurando que las categorías traigan TODO su contenido (_base)
                Entitat entitatCompleta = dataManager.load(Entitat.class)
                        .id(entitat.getId())
                        .fetchPlan(fp -> fp.addFetchPlan(FetchPlan.BASE)
                                .add("categories", FetchPlan.BASE)) // 🛠️ ¡CORREGIDO AQUÍ! Forzamos a cargar el 'nom' de la categoría
                        .optional()
                        .orElse(null);

                if (entitatCompleta != null && entitatCompleta.getCategories() != null && !entitatCompleta.getCategories().isEmpty()) {

                    // Ahora sí, Categoria.getNom() funcionará de forma segura porque está en memoria
                    String nombresCategorias = entitatCompleta.getCategories().stream()
                            .map(Categoria::getNom)
                            .collect(Collectors.joining(", "));
                    entitatCategoriesField.setValue(nombresCategorias);

                    // 4. Copia e inyección automática en la lista propia del Contacto
                    if (contacte.getCategories() == null) {
                        contacte.setCategories(new ArrayList<>());
                    }

                    for (Categoria cat : entitatCompleta.getCategories()) {
                        if (!contacte.getCategories().contains(cat)) {
                            contacte.getCategories().add(cat);
                        }
                    }
                } else {
                    entitatCategoriesField.setValue("Sense categories");
                }

            } else {
                carrecEntitatNomField.setValue("");
                carrecTitolField.setValue("");
                entitatCategoriesField.setValue("");
            }
        } else {
            carrecEntitatNomField.setValue("");
            carrecTitolField.setValue("");
            entitatCategoriesField.setValue("");
        }
    }
}