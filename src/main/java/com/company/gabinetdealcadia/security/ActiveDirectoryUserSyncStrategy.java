package com.company.gabinetdealcadia.security;

import com.company.gabinetdealcadia.entity.User;
import io.jmix.core.DataManager;
import io.jmix.ldap.userdetails.AbstractLdapUserDetailsSynchronizationStrategy;
import io.jmix.security.role.assignment.RoleAssignmentRoleType;
import io.jmix.securitydata.entity.RoleAssignmentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component("arvia_ActiveDirectoryUserSyncStrategy")
public class ActiveDirectoryUserSyncStrategy
        extends AbstractLdapUserDetailsSynchronizationStrategy<User> {

    @Autowired
    protected DataManager dataManager;

    @Override
    public Class<User> getUserClass() {
        return User.class;
    }

    @Override
    protected void mapUserDetailsAttributes(User user, DirContextOperations ctx) {
        // Obtenemos todos los grupos en los que está el usuario en el AD
        String[] memberOfArray = ctx.getStringAttributes("memberOf");

        if (memberOfArray != null) {
            for (String groupDn : memberOfArray) {
                String groupDnUpper = groupDn.toUpperCase();

                // EVALUAMOS: ¿Pertenece a un grupo o a otro?
                if (groupDnUpper.contains("FMALCALDIACONTRIBUTOR")) {
                    asignarRolSiNoExiste(user.getUsername(), ReadAccessRole.CODE);

                } else if (groupDnUpper.contains("FMALCALDIAADMIN")) {
                    asignarRolSiNoExiste(user.getUsername(), "rol-admin-jmix");

                } else if (groupDnUpper.contains("FMALCALDIAREADER")) {
                    asignarRolSiNoExiste(user.getUsername(), "rol-lector-jmix");
                }
            }
        }
    }

    private void asignarRolSiNoExiste(String username, String roleCode) {
        boolean yaTieneRol = dataManager.load(RoleAssignmentEntity.class)
                .query("select e from sec_RoleAssignmentEntity e where e.username = :username and e.roleCode = :roleCode")
                .parameter("username", username)
                .parameter("roleCode", roleCode)
                .optional()
                .isPresent();

        if (!yaTieneRol) {
            RoleAssignmentEntity assignment = dataManager.create(RoleAssignmentEntity.class);
            assignment.setUsername(username);
            assignment.setRoleCode(roleCode);
            assignment.setRoleType(RoleAssignmentRoleType.RESOURCE);
            dataManager.save(assignment);
        }
    }
}