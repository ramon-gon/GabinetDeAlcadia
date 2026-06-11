package com.company.gabinetdealcadia.service;

import com.company.gabinetdealcadia.entity.Carrec;
import com.company.gabinetdealcadia.entity.Contacte;
import com.company.gabinetdealcadia.entity.Entitat;
import io.jmix.core.DataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarrecService {

    @Autowired
    private DataManager dataManager;

    public void separarTelefon(Carrec carrec) {
        String tel = carrec.getTelefonDirecte();
        if (tel != null && tel.trim().length() > 9) {
            String telNete = tel.replaceAll("[^0-9]", "");

            // Guardem els primers 9 dígits com a telèfon directe
            carrec.setTelefonDirecte(telNete.length() > 9 ? telNete.substring(0, 9) : telNete);

            // Si el text original era llarg, en guardem el comentari restant
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
                .fetchPlan(fp -> fp.add("entitat", io.jmix.core.FetchPlan.BASE))
                .optional()
                .map(carrec -> carrec.getEntitat() != null ? carrec.getEntitat().getNom() : null)
                .orElse(null);
    }

    /**
     * Obté l'objecte Carrec vigent d'un contacte, assegurant que es carreguin
     * tant la relació de l'Entitat com el camp Títol del Càrrec per evitar el crash d'unfetched attribute.
     */
    public Carrec obtenirCarrecVigent(Contacte contacte) {
        if (contacte == null) {
            return null;
        }

        return dataManager.load(Carrec.class)
                .query("select c from Carrec c where c.contacte = :contacte and c.vigent = true")
                .parameter("contacte", contacte)
                // MODIFICACIÓ CLAU: Forcem a carregar l'entitat i el títol del càrrec de forma explícita
                .fetchPlan(fp -> {
                    fp.add("entitat", io.jmix.core.FetchPlan.BASE);
                    fp.add("titolCarrec");
                })
                .optional()
                .orElse(null);
    }
}