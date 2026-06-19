package com.company.gabinetdealcadia.view.categoria;

import com.company.gabinetdealcadia.entity.Categoria;
import com.company.gabinetdealcadia.service.DataGridService; // <-- Importamos nuestro servicio
import com.company.gabinetdealcadia.view.main.MainView;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "categorias", layout = MainView.class)
@ViewController("Categoria.list")
@ViewDescriptor("categoria-list-view.xml")
@LookupComponent("categoriasDataGrid")
@DialogMode(width = "64em", height = "43.5em")
public class CategoriaListView extends StandardListView<Categoria> {

    @Autowired
    private DataGridService dataGridService; // 🛠️ Inyectamos el servicio centralizado

    @ViewComponent
    private DataGrid<Categoria> categoriasDataGrid;

    @Subscribe("categoriasDataGrid")
    public void onCategoriasDataGridItemDoubleClick(final ItemClickEvent<Categoria> event) {
        // Delegamos todo el comportamiento al servicio en una sola línea límpia
        dataGridService.handleItemDoubleClick(event, categoriasDataGrid);
    }
}