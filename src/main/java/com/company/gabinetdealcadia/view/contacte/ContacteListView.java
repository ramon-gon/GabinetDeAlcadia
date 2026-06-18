package com.company.gabinetdealcadia.view.contacte;

import com.company.gabinetdealcadia.entity.Contacte;
import com.company.gabinetdealcadia.entity.Carrec;
import com.company.gabinetdealcadia.entity.Categoria;
import com.company.gabinetdealcadia.service.CarrecService;
import com.company.gabinetdealcadia.view.main.MainView;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.core.FetchPlan;
import io.jmix.flowui.action.list.ReadAction;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.view.*;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
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
    private DataManager dataManager;

    // Inyectamos la tabla de contactos de la vista
    @ViewComponent
    private DataGrid<Contacte> contactesDataGrid;

    // 1. Renderer per al nom complet del contacte
    @Supply(to = "contactesDataGrid.nomcompletcontacte", subject = "renderer")
    private Renderer<Contacte> nomCompletContacteRenderer() {
        return new TextRenderer<>(c -> carrecService.formatarNomComplet(c));
    }

    // 2. Renderer per a les entitats associades als càrrecs del contacte
    @Supply(to = "contactesDataGrid.entitatpertanyent", subject = "renderer")
    private Renderer<Contacte> entitatPertanyentRenderer() {
        return new TextRenderer<>(contacte -> {
            List<Carrec> totsElsCarrecs = dataManager.load(Carrec.class)
                    .query("select c from Carrec c where c.contacte = :contacte")
                    .parameter("contacte", contacte)
                    .fetchPlan(fetchPlan -> {
                        fetchPlan.addFetchPlan(FetchPlan.BASE);
                        fetchPlan.add("entitat", FetchPlan.BASE);
                    })
                    .list();

            if (totsElsCarrecs.isEmpty()) {
                return "";
            }

            return totsElsCarrecs.stream()
                    .filter(carrec -> carrec.getEntitat() != null)
                    .map(carrec -> carrec.getEntitat().getNom())
                    .distinct()
                    .collect(Collectors.joining(", "));
        });
    }

    // 3. RENDERER OPTIMIZADO: Primero categorías de Entidad (Bat) y luego las del Contacto
    @Supply(to = "contactesDataGrid.categoriesColumn", subject = "renderer")
    private Renderer<Contacte> categoriesColumnRenderer() {
        return new TextRenderer<>(contacte -> {

            List<Carrec> totsElsCarrecs = dataManager.load(Carrec.class)
                    .query("select c from Carrec c where c.contacte = :contacte")
                    .parameter("contacte", contacte)
                    .fetchPlan(fp -> {
                        fp.addFetchPlan(FetchPlan.BASE);
                        fp.add("entitat", FetchPlan.BASE);
                        fp.add("entitat.categories", FetchPlan.BASE);
                    })
                    .list();

            Contacte reloadContacte = dataManager.load(Contacte.class)
                    .id(contacte.getId())
                    .fetchPlan(fp -> {
                        fp.addFetchPlan(FetchPlan.BASE);
                        fp.add("categories", FetchPlan.BASE);
                    })
                    .optional()
                    .orElse(contacte);

            List<String> catsEntitat = new ArrayList<>();
            List<String> catsContacte = new ArrayList<>();

            for (Carrec carrec : totsElsCarrecs) {
                if (carrec.getEntitat() != null && carrec.getEntitat().getCategories() != null) {
                    for (Categoria cat : carrec.getEntitat().getCategories()) {
                        if (cat.getNom() != null && !catsEntitat.contains(cat.getNom())) {
                            catsEntitat.add(cat.getNom());
                        }
                    }
                }
            }

            if (reloadContacte.getCategories() != null) {
                for (Categoria cat : reloadContacte.getCategories()) {
                    if (cat.getNom() != null) {
                        if (!catsEntitat.contains(cat.getNom()) && !catsContacte.contains(cat.getNom())) {
                            catsContacte.add(cat.getNom());
                        }
                    }
                }
            }

            List<String> llistaFinalUnificada = new ArrayList<>();
            llistaFinalUnificada.addAll(catsEntitat);
            llistaFinalUnificada.addAll(catsContacte);

            if (llistaFinalUnificada.isEmpty()) {
                return "";
            }

            return llistaFinalUnificada.stream()
                    .collect(Collectors.joining(", "));
        });
    }

    // NUEVO: Captura el doble clic y ejecuta de forma segura la visualización
    @Subscribe("contactesDataGrid")
    public void onContactesDataGridItemDoubleClick(final ItemClickEvent<Contacte> event) {
        if (event.getClickCount() == 2 && event.getItem() != null) {
            // Forzamos al DataGrid a seleccionar el elemento antes de lanzar la acción
            contactesDataGrid.select(event.getItem());

            // Ejecutamos el modo solo lectura
            ReadAction<Contacte> readAction = (ReadAction<Contacte>) contactesDataGrid.getAction("readAction");
            if (readAction != null) {
                readAction.execute();
            }
        }
    }
}