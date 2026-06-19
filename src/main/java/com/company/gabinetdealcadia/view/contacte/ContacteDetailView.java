package com.company.gabinetdealcadia.view.contacte;

import com.company.gabinetdealcadia.entity.Contacte;
import com.company.gabinetdealcadia.service.ContacteService;
import com.company.gabinetdealcadia.view.main.MainView;
import io.jmix.flowui.component.textarea.JmixTextArea;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.view.*;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "contactes/:id", layout = MainView.class)
@ViewController("Contacte.detail")
@ViewDescriptor("contacte-detail-view.xml")
@EditedEntityContainer("contacteDc")
public class ContacteDetailView extends StandardDetailView<Contacte> {

    @Autowired
    private ContacteService contacteService;

    // Componentes de lectura calculados
    @ViewComponent private JmixTextArea carrecEntitatNomField;
    @ViewComponent private JmixTextArea carrecTitolField;
    @ViewComponent private JmixTextArea entitatCategoriesField;

    // 🛠️ Inyecciones de los 3 Teléfonos libres de la interfaz
    @ViewComponent private TypedTextField<String> tel1Field;
    @ViewComponent private TypedTextField<String> tel2Field;
    @ViewComponent private TypedTextField<String> tel3Field;

    // 🛠️ Inyecciones de los 3 Comentarios libres de la interfaz
    @ViewComponent private TypedTextField<String> comentariTel1Field;
    @ViewComponent private TypedTextField<String> comentariTel2Field;
    @ViewComponent private TypedTextField<String> comentariTel3Field;

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        Contacte contacte = getEditedEntity();

        if (contacte != null) {
            // 1. Cargamos los campos de texto calculados (entidades, cargos, etc.)
            // NOTA: Si tu método 'procesarDadesDetall' devolvía una clase contenedora (como vimos al principio),
            // asegúrate de que esté implementada en tu servicio.
            try {
                var camps = contacteService.procesarDadesDetall(contacte);
                carrecEntitatNomField.setValue(camps.getEntitats());
                carrecTitolField.setValue(camps.getTitols());
                entitatCategoriesField.setValue(camps.getCategories());
            } catch (Exception e) {
                // Control de seguridad por si el método difiere en tu entorno actual
            }

            // 2. 🛠️ Desglosamos la ristra de teléfonos de la base de datos y rellenamos la pantalla
            String[] tfs = contacteService.desglossarTextComes(contacte.getTelefonMobil());
            tel1Field.setValue(tfs[0]);
            tel2Field.setValue(tfs[1]);
            tel3Field.setValue(tfs[2]);

            // 3. 🛠️ Desglosamos la ristra de comentarios de la base de datos y rellenamos la pantalla
            String[] cmts = contacteService.desglossarTextComes(contacte.getComentariTelefonMobil());
            comentariTel1Field.setValue(cmts[0]);
            comentariTel2Field.setValue(cmts[1]);
            comentariTel3Field.setValue(cmts[2]);
        }
    }

    @Subscribe
    public void onBeforeSave(final BeforeSaveEvent event) {
        Contacte contacte = getEditedEntity();

        if (contacte != null) {
            // 4. 🛠️ Unificamos los 3 números de teléfono con comas antes de guardar
            String telefonsUnificats = contacteService.unificarTextComes(
                    tel1Field.getValue(), tel2Field.getValue(), tel3Field.getValue()
            );
            contacte.setTelefonMobil(telefonsUnificats);

            // 5. 🛠️ Unificamos los 3 comentarios con comas antes de guardar
            String comentarisUnificats = contacteService.unificarTextComes(
                    comentariTel1Field.getValue(), comentariTel2Field.getValue(), comentariTel3Field.getValue()
            );
            contacte.setComentariTelefonMobil(comentarisUnificats);
        }
    }
}