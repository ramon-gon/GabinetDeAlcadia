package com.company.gabinetdealcadia.view.categoria;

import com.company.gabinetdealcadia.entity.Categoria;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.company.gabinetdealcadia.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.action.list.ReadAction; // <-- Importamos ReadAction exactamente igual
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.view.*;

@Route(value = "categorias", layout = MainView.class)
@ViewController("Categoria.list")
@ViewDescriptor("categoria-list-view.xml")
@LookupComponent("categoriasDataGrid")
@DialogMode(width = "64em", height = "43.5em")
public class CategoriaListView extends StandardListView<Categoria> {

    // Inyectamos el componente de la tabla de forma correcta
    @ViewComponent
    private DataGrid<Categoria> categoriasDataGrid;

    @Subscribe("categoriasDataGrid")
    public void onCategoriasDataGridItemDoubleClick(final ItemClickEvent<Categoria> event) {
        if (event.getClickCount() == 2 && event.getItem() != null) {
            // 1. Forzamos la selección del elemento sobre el que se hizo doble clic
            categoriasDataGrid.select(event.getItem());

            // 2. Ahora sí, buscamos la acción y la ejecutamos de forma segura
            ReadAction<Categoria> readAction = (ReadAction<Categoria>) categoriasDataGrid.getAction("readAction");
            if (readAction != null) {
                readAction.execute();
            }
        }
    }
}