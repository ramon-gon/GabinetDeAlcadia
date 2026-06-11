package com.company.gabinetdealcadia.service;

import com.company.gabinetdealcadia.entity.Carrec;
import com.company.gabinetdealcadia.entity.Contacte;
import com.company.gabinetdealcadia.entity.Entitat;
// IMPORTACIONS AFEGIDES: Motor de dades de Jmix i injecció de Spring
import io.jmix.core.DataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarrecService {

    // INJECCIÓ AFEGIDA: Ara el servei ja té accés a la base de dades
    @Autowired
    private DataManager dataManager;

    public void separarTelefon(Carrec carrec) {
        String tel = carrec.getTelefonDirecte();
        if (tel != null && tel.trim().length() > 9) {
            String telNete = tel.replaceAll("[^0-9]", "");

            // Guardem els primers 9 dígits com a telèfon directe
            carrec.setTelefonDirecte(telNete.length() > 9 ? telNete.substring(0, 9) : telNete);

            // Correcció segura: si el text original era llarg, en guardem el comentari restant
            if (tel.trim().length() > 9) {
                carrec.setComentariTelefon(tel.trim().substring(9).trim());
            }
        }
    }

    public String formatarNomComplet(Contacte contacte) {
        if (contacte == null) return "";
        String nom = contacte.getNom() != null ? contacte.getNom() : "";
        String primer = contacte.getPrimer_cognom() != null ? contacte.getPrimer_cognom() : "";
        String segon = contacte.getSegon_cognom() != null ? contacte.getSegon_cognom() : "";
        return String.format("%s %s %s", nom, primer, segon).trim().replaceAll("\\s+", " ");
    }

    public String formatarNomEntitat(Entitat entitat) {
        return (entitat != null && entitat.getNom() != null) ? entitat.getNom() : "";
    }

    public String obtenirNomEntitatVigent(Contacte contacte) {
        if (contacte == null) {
            return null;
        }

        return dataManager.load(Carrec.class)
                .query("select c from Carrec c where c.contacte = :contacte and c.vigent = true")
                .parameter("contacte", contacte)
                // CORRECCIÓ: Sintaxi oficial de Jmix 2.x per a FetchPlanBuilder
                .fetchPlan(fp -> fp.add("entitat", io.jmix.core.FetchPlan.BASE))
                .optional()
                .map(carrec -> carrec.getEntitat() != null ? carrec.getEntitat().getNom() : null)
                .orElse(null);
    }
}