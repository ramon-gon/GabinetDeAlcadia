package com.company.gabinetdealcadia.security;

import io.jmix.ldap.userdetails.LdapAuthorityToJmixRoleCodesMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Mapea los grupos del Active Directory a codigos de rol de Jmix.
 * <p>
 * Se usa en modo {@code jmix.ldap.user-details-source=ldap}: el usuario NO existe
 * en la BD, se crea en memoria tras cada login y sus roles se resuelven aqui.
 * <p>
 * A cada grupo se le anade siempre {@link UiMinimalRole} para conceder el permiso
 * {@code ui.loginToUi}; sin el, el usuario se autentica pero no puede entrar a la UI.
 */
@Component("arvia_LdapGroupRoleMapper")
public class LdapGroupRoleMapper implements LdapAuthorityToJmixRoleCodesMapper {

    @Override
    public Collection<String> mapAuthorityToJmixRoleCodes(String authority) {
        Collection<String> roles = new ArrayList<>();

        // 'authority' es el nombre del grupo LDAP (normalmente el 'cn', aunque en AD
        // puede llegar como DN completo); comparamos en mayusculas para evitar
        // problemas de mayusculas/minusculas.
        String authorityUpper = authority.toUpperCase();

        if (authorityUpper.contains("CONTACTESALCALDIA_ADMIN")) {
            roles.add(FullAccessRole.CODE);
            roles.add(UiMinimalRole.CODE);

        } else if (authorityUpper.contains("CONTACTESALCALDIA_RE")) {
            roles.add(ReadAccessRole.CODE);
            roles.add(UiMinimalRole.CODE);

        }

        return roles;
    }
}