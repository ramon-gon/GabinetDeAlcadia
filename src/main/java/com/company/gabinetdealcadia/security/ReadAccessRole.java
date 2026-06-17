package com.company.gabinetdealcadia.security;

import com.company.gabinetdealcadia.entity.Carrec;
import com.company.gabinetdealcadia.entity.Contacte;
import com.company.gabinetdealcadia.entity.Entitat;
import io.jmix.security.model.EntityAttributePolicyAction;
import io.jmix.security.model.EntityPolicyAction;
import io.jmix.security.role.annotation.EntityAttributePolicy;
import io.jmix.security.role.annotation.EntityPolicy;
import io.jmix.security.role.annotation.ResourceRole;
import io.jmix.securityflowui.role.annotation.MenuPolicy;
import io.jmix.securityflowui.role.annotation.ViewPolicy;

@ResourceRole(name = "ReadAccessRole", code = ReadAccessRole.CODE)
public interface ReadAccessRole {
    String CODE = "read-access-role";

    // 1. PERMISOS DE ENTIDAD: Accés bàsic a les dades
    @EntityPolicy(entityClass = Carrec.class, actions = EntityPolicyAction.READ)
    @EntityPolicy(entityClass = Entitat.class, actions = EntityPolicyAction.READ)
    @EntityPolicy(entityClass = Contacte.class, actions = EntityPolicyAction.READ)
    void entityPermissions();

    // 2. PERMISOS DE ATRIBUTOS: Vital per veure les dades a la taula
    @EntityAttributePolicy(entityClass = Carrec.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = Entitat.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = Contacte.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    void attributePermissions();

    // 3. PERMISOS DE VISTAS: Accés a les pantalles
    @ViewPolicy(viewIds = {"MainView", "Carrec.list", "Carrec.detail", "Entitat.list", "Entitat.detail", "Contacte.list", "Contacte.detail"})
    void viewPermissions();

    // 4. PERMISOS DE MENÚ: Això és el que et farà aparèixer els ítems al menú lateral
    @MenuPolicy(menuIds = {"Entitat.list", "Contacte.list"})
    void menuPermissions();
}