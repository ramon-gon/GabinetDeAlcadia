package com.company.gabinetdealcadia.service;

import com.vaadin.flow.component.grid.ItemClickEvent;
import io.jmix.flowui.action.list.ReadAction;
import io.jmix.flowui.component.grid.DataGrid;
import org.springframework.stereotype.Component;

@Component("gab_DataGridService")
public class DataGridService {

    /**
     * Gestiona el doble clic en cualquier DataGrid de Jmix de forma genérica.
     * Selecciona el elemento y ejecuta la acción especificada (ej. "readAction").
     */
    @SuppressWarnings("unchecked")
    public <E> void handleItemDoubleClick(ItemClickEvent<E> event, DataGrid<E> dataGrid, String actionId) {
        if (event.getClickCount() == 2 && event.getItem() != null) {
            // 1. Forzamos la selección del elemento de forma genérica
            dataGrid.select(event.getItem());

            // 2. Buscamos la acción en el DataGrid y la ejecutamos
            if (dataGrid.getAction(actionId) instanceof ReadAction) {
                ReadAction<E> readAction = (ReadAction<E>) dataGrid.getAction(actionId);
                if (readAction != null) {
                    readAction.execute();
                }
            }
        }
    }

    /**
     * Sobrecarga por defecto para usar "readAction" automáticamente.
     */
    public <E> void handleItemDoubleClick(ItemClickEvent<E> event, DataGrid<E> dataGrid) {
        handleItemDoubleClick(event, dataGrid, "readAction");
    }
}