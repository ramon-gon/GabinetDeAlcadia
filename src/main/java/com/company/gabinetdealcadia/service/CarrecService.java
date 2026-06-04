package com.company.gabinetdealcadia.service;

import com.company.gabinetdealcadia.entity.Carrec;
import com.company.gabinetdealcadia.entity.Contacte;
import com.company.gabinetdealcadia.entity.Entitat;
import org.springframework.stereotype.Service;

@Service
public class CarrecService {

    public void separarTelefon(Carrec carrec) {
        String tel = carrec.getTelefonDirecte();
        if (tel != null && tel.length() > 9) {
            String telNete = tel.replaceAll("[^0-9]", "");
            carrec.setTelefonDirecte(telNete.length() > 9 ? telNete.substring(0, 9) : telNete);
            carrec.setComentariTelefon(tel.substring(9).trim());
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
}