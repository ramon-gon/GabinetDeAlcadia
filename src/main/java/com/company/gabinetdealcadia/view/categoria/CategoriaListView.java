package com.company.gabinetdealcadia.view.categoria;

import com.company.gabinetdealcadia.entity.Categoria;
import io.jmix.flowui.action.list.ReadAction;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.view.*;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.company.gabinetdealcadia.view.main.MainView;
import com.vaadin.flow.router.Route;

@Route(value = "categorias", layout = MainView.class)

// ¡CORREGIDO! Eliminada la anotación @ViewComponent de aquí
@ViewController("Categoria.list")
@ViewDescriptor("categoria-list-view.xml")
@LookupComponent("categoriasDataGrid")
@DialogMode(width = "64em", height = "43.5em")
public class CategoriaListView extends StandardListView<Categoria> {

    // Aquí SÍ va @ViewComponent, porque estás inyectando un componente de la vista
    @ViewComponent
    private DataGrid<Categoria> categoriasDataGrid;

    @Subscribe("categoriasDataGrid")
    public void onCategoriasDataGridItemDoubleClick(final ItemClickEvent<Categoria> event) {
        if (event.getClickCount() == 2) {
            ReadAction<Categoria> readAction = (ReadAction<Categoria>) categoriasDataGrid.getAction("readAction");
            if (readAction != null) {
                readAction.execute();
            }
        }
    }
}
