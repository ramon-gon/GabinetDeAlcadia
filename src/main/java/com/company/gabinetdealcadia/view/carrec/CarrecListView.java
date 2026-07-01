package com.company.gabinetdealcadia.view.carrec;

import com.company.gabinetdealcadia.entity.Carrec;
import com.company.gabinetdealcadia.service.DataGridService; // 🛠️ Importamos el servicio centralizado
import com.company.gabinetdealcadia.view.main.MainView;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired; // 🛠️ Importamos Autowired para Spring

@Route(value = "carrecs", layout = MainView.class)
@ViewController(id = "Carrec.list")
@ViewDescriptor(path = "carrec-list-view.xml")
@LookupComponent("carrecsDataGrid")
@DialogMode(width = "64em")
public class CarrecListView extends StandardListView<Carrec> {

    @Autowired
    private DataGridService dataGridService; // 🛠️ Inyectamos el servicio centralizado

    @ViewComponent
    private DataGrid<Carrec> carrecsDataGrid;

    @Supply(to = "carrecsDataGrid.puntTrameses", subject = "renderer")
    private Renderer<Carrec> puntTramesesRenderer() {
        return new TextRenderer<>(Carrec::getPuntTramesesSiNo);
    }

    @Subscribe("carrecsDataGrid")
    public void onCarrecsDataGridItemDoubleClick(final ItemClickEvent<Carrec> event) {
        // 🛠️ Delegamos toda la lógica repetitiva en una sola línea limpia
        dataGridService.handleItemDoubleClick(event, carrecsDataGrid);
    }
}