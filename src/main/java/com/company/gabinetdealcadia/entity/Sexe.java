package com.company.gabinetdealcadia.entity;

import io.jmix.core.metamodel.datatype.EnumClass;
import org.springframework.lang.Nullable;

public enum Sexe implements EnumClass<String> {

    HOME("HOME"),
    DONA("DONA"),
    ALTRES("ALTRES");

    private final String id;

    Sexe(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Nullable
    public static Sexe fromId(String id) {
        for (Sexe at : Sexe.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}