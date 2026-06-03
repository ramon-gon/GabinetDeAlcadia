package com.company.gabinetdealcadia.view.carrec;

import com.company.gabinetdealcadia.entity.Carrec;
import com.company.gabinetdealcadia.entity.Contacte;
import com.company.gabinetdealcadia.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.renderer.TextRenderer;

@Route(value = "carrecs", layout = MainView.class)
@ViewController("Carrec.list")
@ViewDescriptor("carrec-list-view.xml")
@LookupComponent("carrecsDataGrid")
public class CarrecListView extends StandardListView<Carrec> {

    @Supply(to = "carrecsDataGrid.telefonDirecte", subject = "renderer")
    private Renderer<Carrec> telefonDirecteRenderer() {
        return new TextRenderer<>(c -> {
            String tel = c.getTelefonDirecte();
            if (tel == null || tel.isBlank()) return "";

            // 1. Neteja: Treu espais, guions o caràcters especials per assegurar que els dígits són reals
            String telNete = tel.replaceAll("[^0-9]", "");

            // 2. Tall: Ara el substring(0, 9) sí que agafarà els 9 primers números reals
            return telNete.length() > 9 ? telNete.substring(0, 9) : telNete;
        });
    }

    @Supply(to = "carrecsDataGrid.sobrantTelefonDirecte", subject = "renderer")
    private Renderer<Carrec> sobrantTelefonDirecteRenderer() {
        return new TextRenderer<>(c -> {
            String tel = c.getTelefonDirecte();

            // Comprovem si el camp té contingut
            if (tel == null || tel.isEmpty()) {
                return "";
            }

            // Si el text total és més llarg de 9, tallem a partir del 9è caràcter (índex 9)
            // Això agafarà tot el que ve després, hagi espai o no.
            if (tel.length() > 9) {
                String sobrant = tel.substring(10);

                // Netejem possibles espais sobrants al principi si n'hi hagués
                return sobrant.trim();
            }

            // Si té 9 o menys caràcters, la columna queda buida
            return "";
        });
    }

    @Supply(to = "carrecsDataGrid.vigent", subject = "renderer")
    private com.vaadin.flow.data.renderer.Renderer<Carrec> vigentRenderer() {
        return new com.vaadin.flow.data.renderer.TextRenderer<>(carrec ->
                (carrec.getVigent() != null && carrec.getVigent()) ? "Sí" : "No"
        );
    }

    @Supply(to = "carrecsDataGrid.puntTrameses", subject = "renderer")
    private com.vaadin.flow.data.renderer.Renderer<Carrec> puntTramesesRenderer() {
        return new com.vaadin.flow.data.renderer.TextRenderer<>(carrec ->
                (carrec.getPuntTrameses() != null && carrec.getPuntTrameses()) ? "Sí" : "No"
        );
    }

    @Supply(to = "carrecsDataGrid.nomCompletContacte", subject = "renderer")
    private com.vaadin.flow.data.renderer.Renderer<Carrec> nomCompletContacteRenderer() {
        return new com.vaadin.flow.data.renderer.TextRenderer<>(carrec -> {
            Contacte contacte = carrec.getContacte();
            if (contacte == null) {
                return "";
            }
            String nom = contacte.getNom() != null ? contacte.getNom() : "";
            String primerCognom = contacte.getPrimer_cognom() != null ? contacte.getPrimer_cognom() : "";
            String segonCognom = contacte.getSegon_cognom() != null ? contacte.getSegon_cognom() : "";

            return String.format("%s %s %s", nom, primerCognom, segonCognom).trim().replaceAll("\\s+", " ");
        });
    }
}