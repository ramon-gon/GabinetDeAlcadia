package com.company.gabinetdealcadia.view.entitat;

import com.company.gabinetdealcadia.entity.Entitat;
import com.company.gabinetdealcadia.service.EntitatService;
import com.company.gabinetdealcadia.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "entitats/:id", layout = MainView.class)
@ViewController("Entitat.detail")
@ViewDescriptor("entitat-detail-view.xml")
@EditedEntityContainer("entitatDc")
public class EntitatDetailView extends StandardDetailView<Entitat> {

    @Autowired
    private EntitatService entitatService;

    @ViewComponent
    private TypedTextField<String> presidentField;

    /**
     * S'executa quan la pantalla està completament carregada i a punt.
     */
    @Subscribe
    public void onReady(final ReadyEvent event) {
        // Obtenim l'entitat actual que s'està editant o consultant
        Entitat entitat = getEditedEntity();

        // Busquem el nom del president mitjançant el servei
        String nomPresident = entitatService.obtenirNomPresident(entitat);

        // Assignem el valor al camp de la pantalla
        presidentField.setValue(nomPresident);
    }
}