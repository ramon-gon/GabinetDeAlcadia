package com.company.gabinetdealcadia.view.carrec;

import com.company.gabinetdealcadia.entity.Contacte;
import com.company.gabinetdealcadia.entity.Carrec;
import com.company.gabinetdealcadia.service.CarrecService;
import com.company.gabinetdealcadia.view.main.MainView;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.core.FetchPlan;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Route(value = "carrecs", layout = MainView.class)
@ViewController("Carrec.list")
@ViewDescriptor("carrec-list-view.xml")
@LookupComponent("contactesDataGrid")
public class CarrecListView extends StandardListView<Contacte> { // 🛠️ Cambiado a Contacte

    @Autowired
    private CarrecService carrecService;

    @Autowired
    private DataManager dataManager;

    // 1. Renderiza el nombre del contacto una sola vez por fila
    @Supply(to = "contactesDataGrid.nomCompletContacte", subject = "renderer")
    private Renderer<Contacte> nomCompletContacteRenderer() {
        return new TextRenderer<>(carrecService::formatarNomComplet);
    }

    // 🌟 2. EL TRUCO: Busca todos los cargos de este contacto y los junta en una única línea
    @Supply(to = "contactesDataGrid.cargosAgrupados", subject = "renderer")
    private Renderer<Contacte> cargosAgrupadosRenderer() {
        return new TextRenderer<>(contacte -> {
            // Buscamos los cargos asociados a este contacto en concreto cargando su Entidad de forma segura
            List<Carrec> listaCargos = dataManager.load(Carrec.class)
                    .query("select c from Carrec c where c.contacte = :contacte")
                    .parameter("contacte", contacte)
                    .fetchPlan(fp -> fp.addFetchPlan(FetchPlan.BASE).add("entitat", FetchPlan.BASE))
                    .list();

            if (listaCargos.isEmpty()) {
                return "Sense càrrecs assignats";
            }

            // Los formateamos y unimos en una cadena de texto (ej: "President (Òmnium), Vocal (ANC)")
            return listaCargos.stream()
                    .map(c -> {
                        String titulo = (c.getTitolCarrec() != null) ? c.getTitolCarrec() : "Càrrec";
                        String entitat = (c.getEntitat() != null && c.getEntitat().getNom() != null)
                                ? c.getEntitat().getNom()
                                : "Entitat desconeguda";

                        // Añadimos indicador visual si el cargo no es vigente para dar más información
                        String vigencia = Boolean.TRUE.equals(c.getVigent()) ? "" : " (Històric)";

                        return titulo + " a " + entitat + vigencia;
                    })
                    .collect(Collectors.joining(" | ")); // Separador limpio entre cargos
        });
    }
}