package com.company.gabinetdealcadia.view.contacte;

import com.company.gabinetdealcadia.entity.Contacte;
import com.company.gabinetdealcadia.entity.Carrec;
import com.company.gabinetdealcadia.view.main.MainView;
import com.company.gabinetdealcadia.service.CarrecService;
import com.vaadin.flow.component.textfield.TextField; // IMPORTANT: Importació de Vaadin pura
import io.jmix.flowui.view.*;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "contactes/:id", layout = MainView.class)
@ViewController("Contacte.detail")
@ViewDescriptor("contacte-detail-view.xml")
@EditedEntityContainer("contacteDc")
public class ContacteDetailView extends StandardDetailView<Contacte> {

    @Autowired
    private CarrecService carrecService;

    // Injectem fent servir el TextField de Vaadin que mapeja directament qualsevol <textField> de l'XML
    @ViewComponent
    private TextField carrecEntitatNomField;

    @ViewComponent
    private TextField carrecTitolField;

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        Contacte contacte = getEditedEntity();

        if (contacte != null) {
            Carrec carrecVigent = carrecService.obtenirCarrecVigent(contacte);

            if (carrecVigent != null) {
                // 1. Assignem el Nom de l'Entitat
                if (carrecVigent.getEntitat() != null && carrecVigent.getEntitat().getNom() != null) {
                    carrecEntitatNomField.setValue(carrecVigent.getEntitat().getNom());
                } else {
                    carrecEntitatNomField.setValue("");
                }

                // 2. Assignem el Títol del Càrrec (getTitolCarrec() de la teva entitat Carrec)
                if (carrecVigent.getTitolCarrec() != null) {
                    carrecTitolField.setValue(carrecVigent.getTitolCarrec());
                } else {
                    carrecTitolField.setValue("x");
                }
            } else {
                carrecEntitatNomField.setValue("");
                carrecTitolField.setValue("");
            }
        } else {
            carrecEntitatNomField.setValue("");
            carrecTitolField.setValue("");
        }
    }
}