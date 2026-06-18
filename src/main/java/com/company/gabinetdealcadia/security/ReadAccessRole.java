package com.company.gabinetdealcadia.security;

import com.company.gabinetdealcadia.entity.Carrec;
import com.company.gabinetdealcadia.entity.Contacte;
import com.company.gabinetdealcadia.entity.Entitat;
import com.company.gabinetdealcadia.entity.Categoria;
// Si tienes una entidad para los correos/envíos (por ejemplo Enviament o Email), añade su import aquí:
// import com.company.gabinetdealcadia.entity.Enviament;

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

    // 1. PERMISOS DE ENTIDAD: Accés a la base de dades
    @EntityPolicy(entityClass = Carrec.class, actions = EntityPolicyAction.READ)
    @EntityPolicy(entityClass = Entitat.class, actions = EntityPolicyAction.READ)
    @EntityPolicy(entityClass = Contacte.class, actions = EntityPolicyAction.READ)
    @EntityPolicy(entityClass = Categoria.class, actions = EntityPolicyAction.READ)
    // 🌟 Si usas el sistema de Email nativo de Jmix o tienes tu propia entidad de envíos, la añadimos aquí:
    // @EntityPolicy(entityClass = Enviament.class, actions = EntityPolicyAction.READ)
    void entityPermissions();

    // 2. PERMISOS DE ATRIBUTOS: Vital para pintar los campos en las tablas
    @EntityAttributePolicy(entityClass = Carrec.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = Entitat.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = Contacte.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = Categoria.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    // @EntityAttributePolicy(entityClass = Enviament.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    void attributePermissions();

    // 3. PERMISOS DE VISTAS: Acceso físico a abrir las pantallas
    @ViewPolicy(viewIds = {
            "MainView",
            "Carrec.list", "Carrec.detail",
            "Entitat.list", "Entitat.detail",
            "Contacte.list", "Contacte.detail",
            "Categoria.list", "Categoria.detail", // 🌟 AÑADIDO: Pantallas de Categoría
            "Enviament.list", "Enviament.detail", // 🌟 AÑADIDO: Pantallas de Envíos / Correo ("kayak")
            "Envia.view"                          // Por si tienes una pantalla de formulario directo para enviar
    })
    void viewPermissions();

    // 4. PERMISOS DE MENÚ: Hace que aparezcan los botones en la barra lateral izquierda
    @MenuPolicy(menuIds = {
            "Entitat.list",
            "Contacte.list",
            "Categoria.list",  // 🌟 AÑADIDO: Menú para ir a ver y gestionar Categorías
            "Carrec.list"   // 🌟 AÑADIDO: Menú para ir a ver los Envíos de Correo Electronico
    })
    void menuPermissions();
}