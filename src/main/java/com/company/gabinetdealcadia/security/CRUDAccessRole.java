package com.company.gabinetdealcadia.security;

import com.company.gabinetdealcadia.entity.Carrec;
import com.company.gabinetdealcadia.entity.Contacte;
import com.company.gabinetdealcadia.entity.Entitat;
import io.jmix.security.model.EntityAttributePolicyAction;
import io.jmix.security.model.EntityPolicyAction;
import io.jmix.security.role.annotation.*;
import io.jmix.securityflowui.role.annotation.MenuPolicy;
import io.jmix.securityflowui.role.annotation.ViewPolicy;

@ResourceRole(name = "CRUDAccessRole", code = CRUDAccessRole.CODE)
public interface CRUDAccessRole {
    String CODE = "c-r-u-d-access-role";

    // 1. Permisos d'Entitat (Accions sobre les files)
    @EntityPolicy(entityClass = Carrec.class, actions = EntityPolicyAction.ALL)
    @EntityPolicy(entityClass = Contacte.class, actions = EntityPolicyAction.ALL)
    @EntityPolicy(entityClass = Entitat.class, actions = EntityPolicyAction.ALL)
    void entityPermissions();

    // 2. Permisos d'Atributs (Accions sobre les columnes - CRUCIAL PER EVITAR EL MISSATGE D'ERROR)
    // Utilitzem VIEW per a lectura i MODIFY per a edició
    @EntityAttributePolicy(entityClass = Carrec.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = Contacte.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = Entitat.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    void attributePermissions();

    // 3. Permisos de Vistes
    @ViewPolicy(viewIds = {
            "Carrec.list", "Carrec.detail",
            "Contacte.list", "Contacte.detail",
            "Entitat.list", "Entitat.detail",
            "EnviarCorreu"
    })
    void viewPermissions();

    // 4. Permisos de Menú
    @MenuPolicy(menuIds = {"Carrec.list", "Contacte.list", "Entitat.list", "EnviarCorreu"})
    void menuPermissions();

    // 5. Permís específic per a l'enviament de correus
    @SpecificPolicy(resources = "io.jmix.email.send")
    void emailPermissions();
}