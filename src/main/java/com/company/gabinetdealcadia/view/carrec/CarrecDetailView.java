package com.company.gabinetdealcadia.view.carrec;

import com.company.gabinetdealcadia.entity.Carrec;
import com.company.gabinetdealcadia.entity.Contacte;
import com.company.gabinetdealcadia.entity.Entitat;
import com.company.gabinetdealcadia.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.component.combobox.EntityComboBox;
import io.jmix.flowui.view.*;

@Route(value = "carrecs/:id", layout = MainView.class)
@ViewController("Carrec.detail")
@ViewDescriptor("carrec-detail-view.xml")
@EditedEntityContainer("carrecDc")
public class CarrecDetailView extends StandardDetailView<Carrec> {

    @ViewComponent
    private EntityComboBox<Contacte> contacteField;

    @ViewComponent
    private EntityComboBox<Entitat> entitatField;

    // 🛠️ LA SOLUCIÓ REIAL EN FLOWUI:
    // Configurem el text que es renderitzarà dins del selector quan la pantalla s'iniciï.
    @Subscribe
    public void onInit(final InitEvent event) {

        // 1. Forcem que surti el Nom + Primer Cognom + Segon Cognom complet al combo
        contacteField.setItemLabelGenerator(contacte -> {
            if (contacte == null) return "";
            String nom = contacte.getNom() != null ? contacte.getNom() : "";
            String pCognom = contacte.getPrimer_cognom() != null ? contacte.getPrimer_cognom() : "";
            String sCognom = contacte.getSegon_cognom() != null ? contacte.getSegon_cognom() : "";

            return String.format("%s %s %s", nom, pCognom, sCognom).trim().replaceAll("\\s+", " ");
        });

        // 2. Forcem que surti el text de l'atribut correcte (nom) de l'Entitat
        entitatField.setItemLabelGenerator(entitat -> {
            if (entitat == null) return "";
            return entitat.getNom() != null ? entitat.getNom() : "";
        });
    }
}