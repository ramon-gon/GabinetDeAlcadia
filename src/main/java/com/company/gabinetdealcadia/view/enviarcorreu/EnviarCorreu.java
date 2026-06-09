package com.company.gabinetdealcadia.view.enviarcorreu;

import com.company.gabinetdealcadia.entity.Carrec;
import com.company.gabinetdealcadia.entity.Entitat;
import com.company.gabinetdealcadia.view.main.MainView;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.email.EmailInfoBuilder;
import io.jmix.email.Emailer;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.component.textarea.JmixTextArea;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashSet;
import java.util.Set;

@Route(value = "enviar-correu", layout = MainView.class)
@ViewController("EnviarCorreu")
@ViewDescriptor("enviar-correu.xml")
public class EnviarCorreu extends StandardView {

    @ViewComponent private CollectionLoader<Carrec> contactesFiltratsDl;
    @ViewComponent private CollectionContainer<Carrec> contactesFiltratsDc;
    @ViewComponent private MultiSelectComboBox<Carrec> contactesMultiSelector;
    @ViewComponent private ComboBox<Entitat> entitatFilter;
    @ViewComponent private TypedTextField<String> assumpteField;
    @ViewComponent private JmixTextArea missatgeField;
    @ViewComponent private Button sendBtn;

    @Autowired private Emailer emailer;
    @Autowired private Notifications notifications;
    @Autowired private DataManager dataManager;

    @Subscribe
    public void onInit(final InitEvent event) {
        // 1. Carreguem les entitats pel desplegable
        entitatFilter.setItems(dataManager.load(Entitat.class)
                .query("select distinct cr.entitat from Carrec cr where cr.puntTrameses = true and (cr.emailCorporatiu is not null and cr.emailCorporatiu <> '')")
                .list());

        // 2. Inicialitzem la llista de contactes (com a buida o completa segons el que vulguis per defecte)
        // Si vols que surtin tots al principi, posem la consulta base:
        contactesFiltratsDl.setQuery("select cr from Carrec cr where cr.puntTrameses = true and cr.contacte.emailPersonal is not null");
        contactesFiltratsDl.load();

        // 3. Configura el label (lògica de visualització)
        contactesMultiSelector.setItemLabelGenerator(cr -> {
            // Obtenim l'entitat actual si n'hi ha una de seleccionada
            Entitat entitatSeleccionada = entitatFilter.getValue();

            boolean usarCorp = (entitatSeleccionada != null && cr.getEmailCorporatiu() != null && !cr.getEmailCorporatiu().isEmpty());
            String email = usarCorp ? cr.getEmailCorporatiu() : cr.getContacte().getEmailPersonal();

            String segonCognom = (cr.getContacte().getSegon_cognom() != null) ? cr.getContacte().getSegon_cognom() : "";

            return String.format("%s %s %s <%s>",
                    cr.getContacte().getNom(),
                    cr.getContacte().getPrimer_cognom(),
                    segonCognom,
                    (email != null ? email : "Sense email")).replace("  ", " ");
        });

        contactesMultiSelector.setItems(contactesFiltratsDc.getItems());

        // Si el botó està deshabilitat, a vegades és perquè Jmix creu
        // que la vista està en estat de només lectura o sense dades.
        // Assegura't de forçar l'habilitació si ho desitges:
        sendBtn.setEnabled(true);
    }
    @Subscribe("entitatFilter")
    public void onEntitatFilterChange(final AbstractField.ComponentValueChangeEvent<ComboBox<Entitat>, Entitat> event) {
        Entitat entitat = event.getValue();
        contactesMultiSelector.clear();

        // 1. Netejem els paràmetres existents abans de posar els nous
        contactesFiltratsDl.removeParameter("entitat");

        if (entitat == null) {
            contactesFiltratsDl.setQuery("select cr from Carrec cr where cr.puntTrameses = true and cr.contacte.emailPersonal is not null");
        } else {
            // 2. Aquí definim la consulta AMB el paràmetre :entitat
            contactesFiltratsDl.setQuery("select cr from Carrec cr where cr.entitat = :entitat and cr.puntTrameses = true and cr.emailCorporatiu is not null and cr.emailCorporatiu <> ''");
            // 3. Passem el paràmetre DESPRÉS de setQuery
            contactesFiltratsDl.setParameter("entitat", entitat);
        }

        contactesFiltratsDl.load();
        contactesMultiSelector.getDataProvider().refreshAll();
    }

    @Subscribe("sendBtn")
    public void onSendBtnClick(final ClickEvent<Button> event) {
        Set<Carrec> seleccionats = contactesMultiSelector.getValue();
        if (seleccionats == null || seleccionats.isEmpty()) return;

        Entitat entitatActual = entitatFilter.getValue();
        int exits = 0, errors = 0;

        for (Carrec cr : seleccionats) {
            // Lògica d'elecció de correu en el moment d'enviar
            String emailDesti = (entitatActual != null && cr.getEmailCorporatiu() != null)
                    ? cr.getEmailCorporatiu()
                    : cr.getContacte().getEmailPersonal();

            if (emailDesti != null && !emailDesti.isEmpty()) {
                try {
                    emailer.sendEmail(EmailInfoBuilder.create()
                            .setAddresses(emailDesti)
                            .setSubject(assumpteField.getValue())
                            .setBody(missatgeField.getValue())
                            .build());
                    exits++;
                } catch (Exception e) {
                    errors++;
                }
            }
        }
        notifications.create("Enviats: " + exits + " | Errors: " + errors).show();
        if (errors == 0) closeWithDefaultAction();
    }
}