package com.company.gabinetdealcadia.view.carrec;

import com.company.gabinetdealcadia.entity.Carrec;
import com.company.gabinetdealcadia.entity.Contacte;
import com.company.gabinetdealcadia.entity.Entitat;
import com.company.gabinetdealcadia.service.CarrecService;
import com.company.gabinetdealcadia.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.component.combobox.EntityComboBox;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "carrecs/:id", layout = MainView.class)
@ViewController("Carrec.detail")
@ViewDescriptor("carrec-detail-view.xml")
@EditedEntityContainer("carrecDc")
public class CarrecDetailView extends StandardDetailView<Carrec> {

    @ViewComponent
    private EntityComboBox<Contacte> contacteField;
    @ViewComponent
    private EntityComboBox<Entitat> entitatField;

    @Autowired
    private CarrecService carrecService;

    @Subscribe
    public void onInit(final InitEvent event) {
        // Utilitzem el servei per delegar la lògica del text
        contacteField.setItemLabelGenerator(carrecService::formatarNomComplet);
        entitatField.setItemLabelGenerator(carrecService::formatarNomEntitat);
    }

    @Subscribe(target = Target.DATA_CONTEXT)
    public void onBeforeSave(DataContext.PreSaveEvent event) {
        // Utilitzem el servei per la lògica de negoci abans de desar
        carrecService.separarTelefon(getEditedEntity());
    }
}