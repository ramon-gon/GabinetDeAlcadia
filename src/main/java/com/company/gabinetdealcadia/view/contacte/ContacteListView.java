package com.company.gabinetdealcadia.view.contacte;

import com.company.gabinetdealcadia.entity.Contacte;
import com.company.gabinetdealcadia.entity.Carrec;
import com.company.gabinetdealcadia.service.CarrecService;
import com.company.gabinetdealcadia.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.flowui.view.*;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.stream.Collectors;

@Route(value = "contactes", layout = MainView.class)
@ViewController("Contacte.list")
@ViewDescriptor("contacte-list-view.xml")
@LookupComponent("contactesDataGrid")
@DialogMode(width = "64em")
public class ContacteListView extends StandardListView<Contacte> {

    @Autowired
    private CarrecService carrecService;

    @Autowired
    private DataManager dataManager; // Gestor de datos para lanzar la consulta

    // 1. renderer per al nom complet del contacte
    @Supply(to = "contactesDataGrid.nomcompletcontacte", subject = "renderer")
    private Renderer<Contacte> nomCompletContacteRenderer() {
        return new TextRenderer<>(c -> carrecService.formatarNomComplet(c));
    }

    // 2. 🌟 SOLUCIÓN DEFINITIVA: Busca TODOS los cargos de este contacto en la base de datos
    @Supply(to = "contactesDataGrid.entitatpertanyent", subject = "renderer")
    private Renderer<Contacte> entitatPertanyentRenderer() {
        return new TextRenderer<>(contacte -> {

            // 🛠️ Hacemos una consulta directa a la tabla Carrec para traer TODOS los cargos de este contacto
            List<Carrec> totsElsCarrecs = dataManager.load(Carrec.class)
                    .query("select c from Carrec c where c.contacte = :contacte")
                    .parameter("contacte", contacte)
                    .fetchPlan(fetchPlan -> {
                        fetchPlan.addFetchPlan(io.jmix.core.FetchPlan.BASE);
                        fetchPlan.add("entitat", io.jmix.core.FetchPlan.BASE); // Cargamos su entidad
                    })
                    .list();

            if (totsElsCarrecs.isEmpty()) {
                return "";
            }

            // Ahora que tenemos la lista real de la base de datos, extraemos los nombres y los unimos con comas
            return totsElsCarrecs.stream()
                    .filter(carrec -> carrec.getEntitat() != null)
                    .map(carrec -> carrec.getEntitat().getNom())
                    .distinct() // Evita duplicados si tiene dos cargos en la misma entidad
                    .collect(Collectors.joining(", "));
        });
    }
}