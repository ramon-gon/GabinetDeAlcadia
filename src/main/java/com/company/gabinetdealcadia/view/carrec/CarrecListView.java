package com.company.gabinetdealcadia.view.carrec;

import com.company.gabinetdealcadia.entity.Carrec;
import com.company.gabinetdealcadia.view.main.MainView;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.action.list.ReadAction;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.view.*;

@Route(value = "carrecs", layout = MainView.class)
@ViewController(id = "Carrec.list")
@ViewDescriptor(path = "carrec-list-view.xml")
@LookupComponent("carrecsDataGrid")
@DialogMode(width = "64em")
public class CarrecListView extends StandardListView<Carrec> {

    // Inyectamos el componente de la tabla de forma correcta
    @ViewComponent
    private DataGrid<Carrec> carrecsDataGrid;

    @Supply(to = "carrecsDataGrid.vigent", subject = "renderer")
    private Renderer<Carrec> vigentRenderer() {
        return new TextRenderer<>(Carrec::getVigentSiNo);
    }

    @Supply(to = "carrecsDataGrid.puntTrameses", subject = "renderer")
    private Renderer<Carrec> puntTramesesRenderer() {
        return new TextRenderer<>(Carrec::getPuntTramesesSiNo);
    }

    @Subscribe("carrecsDataGrid")
    public void onCarrecsDataGridItemDoubleClick(final ItemClickEvent<Carrec> event) {
        if (event.getClickCount() == 2 && event.getItem() != null) {
            // 1. Forzamos la selección del elemento sobre el que se hizo doble clic
            carrecsDataGrid.select(event.getItem());

            // 2. Ahora sí, buscamos la acción y la ejecutamos de forma segura
            ReadAction<Carrec> readAction = (ReadAction<Carrec>) carrecsDataGrid.getAction("readAction");
            if (readAction != null) {
                readAction.execute();
            }
        }
    }
}