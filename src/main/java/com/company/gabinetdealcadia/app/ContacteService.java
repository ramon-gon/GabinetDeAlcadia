package com.company.gabinetdealcadia.app;

import com.company.gabinetdealcadia.entity.Contacte;
import org.springframework.stereotype.Component;

@Component
public class ContacteService {
    public void processarTelefonMobil(Contacte contacte) {
        String tel = contacte.getTelefonMobil();
        if (tel != null && tel.length() > 9) {
            String telNete = tel.replaceAll("[^0-9]", "");
            contacte.setTelefonMobil(telNete.length() > 9 ? telNete.substring(0, 9) : telNete);
            contacte.setComentariTelefonMobil(tel.substring(9).trim());
        }
    }
}