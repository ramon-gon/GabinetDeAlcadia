package com.company.gabinetdealcadia.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@JmixEntity
@Table(name = "categoria")
@Entity
public class Categoria {

    @JmixGeneratedValue
    @Id
    @Column(name = "id_categoria", nullable = false)
    private UUID id;

    @InstanceName
    @NotNull
    @Column(name = "nom", nullable = false, unique = true)
    private String nom;

    @ManyToMany(mappedBy = "categories")
    private List<Contacte> contactes;

    @ManyToMany(mappedBy = "categories")
    private List<Entitat> entitats;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Contacte> getContactes() {
        return contactes;
    }

    public void setContactes(List<Contacte> contactes) {
        this.contactes = contactes;
    }

    public List<Entitat> getEntitats() {
        return entitats;
    }

    public void setEntitats(List<Entitat> entitats) {
        this.entitats = entitats;
    }
}