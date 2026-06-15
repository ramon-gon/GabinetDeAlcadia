package com.company.gabinetdealcadia.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@JmixEntity
@Table(name = "CATEGORIA")
@Entity
public class Categoria {

    @JmixGeneratedValue
    @Id
    @Column(name = "ID", nullable = false)
    private UUID id;

    @InstanceName
    @NotNull
    @Column(name = "NOM", nullable = false, unique = true)
    private String nom;

    // Relació Molts-a-Molts amb Contacte (Corregida clau inversa)
    @JoinTable(name = "CATEGORIA_CONTACTE_LINK",
            joinColumns = @JoinColumn(name = "CATEGORIA_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "CONTACTE_ID", referencedColumnName = "id_contacte")) // <--- CAMBIADO AQUÍ
    @ManyToMany
    private List<Contacte> contactes;

    // Relació Molts-a-Molts amb Entitat (Corregida clau inversa)
    @JoinTable(name = "CATEGORIA_ENTITAT_LINK",
            joinColumns = @JoinColumn(name = "CATEGORIA_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "ENTITAT_ID", referencedColumnName = "id_entitat")) // <--- CAMBIADO AQUÍ
    @ManyToMany
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