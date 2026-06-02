package com.company.gabinetdealcadia.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true) // autoApply hace que se aplique automáticamente a todos los campos tipo Sexe
public class SexeConverter implements AttributeConverter<Sexe, String> {

    @Override
    public String convertToDatabaseColumn(Sexe attribute) {
        return attribute == null ? null : attribute.getId();
    }

    @Override
    public Sexe convertToEntityAttribute(String dbData) {
        return dbData == null ? null : Sexe.fromId(dbData);
    }
}