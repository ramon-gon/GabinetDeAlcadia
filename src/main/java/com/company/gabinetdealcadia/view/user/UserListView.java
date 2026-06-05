package com.company.gabinetdealcadia.view.user;

import com.company.gabinetdealcadia.entity.User;
import com.company.gabinetdealcadia.view.main.MainView;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;

@Route(value = "usuaris", layout = MainView.class)
@ViewController("User.list")
@ViewDescriptor("user-list-view.xml")
@LookupComponent("usersDataGrid")
public class UserListView extends StandardListView<User> {

    @Supply(to = "usersDataGrid.active", subject = "renderer")
    private Renderer<User> activeRenderer() {
        return new TextRenderer<>(user ->
                Boolean.TRUE.equals(user.getActive()) ? "Sí" : "No"
        );
    }
}