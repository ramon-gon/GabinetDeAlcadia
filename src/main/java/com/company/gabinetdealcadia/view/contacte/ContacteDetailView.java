package com.company.gabinetdealcadia.view.contacte;

import com.company.gabinetdealcadia.entity.Contacte;
import com.company.gabinetdealcadia.entity.Carrec;
import com.company.gabinetdealcadia.entity.Entitat;
import com.company.gabinetdealcadia.entity.Categoria;
import com.company.gabinetdealcadia.view.main.MainView;
import com.company.gabinetdealcadia.service.CarrecService;
import io.jmix.flowui.component.textarea.JmixTextArea;
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
    private JmixTextArea carrecEntitatNomField;

    @ViewComponent
    private JmixTextArea carrecTitolField;

    @ViewComponent
    private JmixTextArea entitatCategoriesField;

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        Contacte contacte = getEditedEntity();

        if (contacte != null) {
            // Buscamos TODOS los cargos reales del contacto en la base de datos
            List<Carrec> totsElsCarrecs = dataManager.load(Carrec.class)
                    .query("select c from Carrec c where c.contacte = :contacte")
                    .parameter("contacte", contacte)
                    .fetchPlan(fp -> {
                        fp.addFetchPlan(FetchPlan.BASE);
                        fp.add("entitat", FetchPlan.BASE);
                        fp.add("entitat.categories", FetchPlan.BASE);
                    })
                    .list();

            if (!totsElsCarrecs.isEmpty()) {

                // Mapeamos y concatenamos TODAS las entidades asociadas (aquí dejamos el distinct por si está dos veces en la misma entidad)
                String nomEntitats = totsElsCarrecs.stream()
                        .filter(c -> c.getEntitat() != null)
                        .map(c -> c.getEntitat().getNom())
                        .distinct()
                        .collect(Collectors.joining(", "));
                carrecEntitatNomField.setValue(nomEntitats);

                // 🌟 MODIFICADO: Hemos quitado '.distinct()' para que aparezcan TODOS los títulos aunque se repita el nombre del cargo
                String titolsCarrecs = totsElsCarrecs.stream()
                        .map(c -> c.getTitolCarrec() != null ? c.getTitolCarrec() : "x")
                        .collect(Collectors.joining(", "));
                carrecTitolField.setValue(titolsCarrecs);

                // Recolectamos todas las categorías únicas de todas sus organizaciones
                List<Categoria> categoriesDeLesEntitats = totsElsCarrecs.stream()
                        .filter(c -> c.getEntitat() != null && c.getEntitat().getCategories() != null)
                        .flatMap(c -> c.getEntitat().getCategories().stream())
                        .distinct()
                        .collect(Collectors.toList());

                if (!categoriesDeLesEntitats.isEmpty()) {
                    String nomsCategories = categoriesDeLesEntitats.stream()
                            .map(Categoria::getNom)
                            .collect(Collectors.joining(", "));
                    entitatCategoriesField.setValue(nomsCategories);

                    // Inyección automática de todas las categorías recolectadas en el propio Contacto
                    if (contacte.getCategories() == null) {
                        contacte.setCategories(new ArrayList<>());
                    }
                    for (Categoria cat : categoriesDeLesEntitats) {
                        if (!contacte.getCategories().contains(cat)) {
                            contacte.getCategories().add(cat);
                        }
                    }
                } else {
                    entitatCategoriesField.setValue("Sense categories");
                }

            } else {
                buidarCampsMulti();
            }
        } else {
            buidarCampsMulti();
        }
    }

    private void buidarCampsMulti() {
        carrecEntitatNomField.setValue("");
        carrecTitolField.setValue("");
        entitatCategoriesField.setValue("");
    }
}