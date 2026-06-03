package com.company.gabinetdealcadia.view.contacte;

import com.company.gabinetdealcadia.entity.Contacte;
import com.company.gabinetdealcadia.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;
import io.jmix.flowui.component.grid.DataGrid;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.renderer.TextRenderer;

@Route(value = "contactes", layout = MainView.class)
@ViewController("Contacte.list")
@ViewDescriptor("contacte-list-view.xml")
public class ContacteListView extends StandardListView<Contacte> {

    // Renderitzador per a la columna del telèfon (només els 9 primers)
    @Supply(to = "contactesDataGrid.telefonMobil", subject = "renderer")
    private Renderer<Contacte> telefonMobilRenderer() {
        return new TextRenderer<>(c -> {
            String tel = c.getTelefonMobil();
            if (tel == null || tel.isEmpty()) return "";
            // Agafa només els 9 primers caràcters
            return tel.length() > 9 ? tel.substring(0, 9) : tel;
        });
    }

    // Renderitzador per a la columna virtual (la resta)
    @Supply(to = "contactesDataGrid.comentarisTelefon", subject = "renderer")
    private Renderer<Contacte> comentarisTelefonRenderer() {
        return new TextRenderer<>(c -> {
            String tel = c.getTelefonMobil();
            // Si el telèfon és més llarg de 9, mostra el que sobra
            if (tel != null && tel.length() > 9) {
                return tel.substring(9).trim();
            }
            return ""; // Si no hi ha més text, deixa la cel·la buida
        });
    }

    // 1. Renderitzador per agrupar el Nom i Cognoms del Contacte principal
    @Supply(to = "contactesDataGrid.nomCompletContacte", subject = "renderer")
    private Renderer<Contacte> nomCompletContacteRenderer() {
        return new TextRenderer<>(contacte -> {
            String nom = contacte.getNom() != null ? contacte.getNom() : "";
            String primerCognom = contacte.getPrimer_cognom() != null ? contacte.getPrimer_cognom() : "";
            String segonCognom = contacte.getSegon_cognom() != null ? contacte.getSegon_cognom() : "";

            String complet = String.format("%s %s %s", nom, primerCognom, segonCognom).trim();
            return complet.replaceAll("\\s+", " ");
        });
    }

    // 2. Renderitzador per agrupar el Nom i Cognoms del Cònjuge
    @Supply(to = "contactesDataGrid.nomCompletConjuge", subject = "renderer")
    private Renderer<Contacte> nomCompletConjugeRenderer() {
        return new TextRenderer<>(contacte -> {
            String nom = contacte.getNomConjuge() != null ? contacte.getNomConjuge() : "";
            String primerCognom = contacte.getPrimerCognomConjuge() != null ? contacte.getPrimerCognomConjuge() : "";
            String segonCognom = contacte.getSegonCognomConjuge() != null ? contacte.getSegonCognomConjuge() : "";

            String complet = String.format("%s %s %s", nom, primerCognom, segonCognom).trim();
            // Si el cònjuge no existeix o està tot buit, retornarà una cel·la buida elegantment
            return complet.isEmpty() ? "" : complet.replaceAll("\\s+", " ");
        });
    }
}