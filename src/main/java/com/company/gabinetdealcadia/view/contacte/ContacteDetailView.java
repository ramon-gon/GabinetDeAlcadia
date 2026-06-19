package com.company.gabinetdealcadia.view.contacte;

import com.company.gabinetdealcadia.entity.Contacte;
import com.company.gabinetdealcadia.service.ContacteService; // 🛠️ Inyectamos el servicio unificado
import com.company.gabinetdealcadia.view.main.MainView;
import io.jmix.flowui.component.textarea.JmixTextArea;
import io.jmix.flowui.view.*;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "contactes/:id", layout = MainView.class)
@ViewController("Contacte.detail")
@ViewDescriptor("contacte-detail-view.xml")
@EditedEntityContainer("contacteDc")
public class ContacteDetailView extends StandardDetailView<Contacte> {

    @Autowired
    private ContacteService contacteService; // 🛠️ Único servicio para gestionar el negocio de contactos

    @ViewComponent
    private JmixTextArea carrecEntitatNomField;

    @ViewComponent
    private JmixTextArea carrecTitolField;

    @ViewComponent
    private JmixTextArea entitatCategoriesField;

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        Contacte contacte = getEditedEntity();

        // 🛠️ Toda la magia y lógica pesada se delega al método que añadimos al servicio
        ContacteService.DetallCampsProcesats camps = contacteService.procesarDadesDetall(contacte);

        // Pasamos los textos directamente a los campos visuales de Jmix
        carrecEntitatNomField.setValue(camps.getEntitats());
        carrecTitolField.setValue(camps.getTitols());
        entitatCategoriesField.setValue(camps.getCategories());
    }
}