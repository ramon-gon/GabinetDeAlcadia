package com.company.gabinetdealcadia.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
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
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @NotNull
    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    // ◄ NUEVA RELACIÓN: Enlace bidireccional con Entitat
    // El "mappedBy" debe llamarse exactamente igual que la propiedad de tipo List<Categoria> que tengas en tu clase Entitat.java (seguramente "categorias")
    @ManyToMany(mappedBy = "categorias")
    private List<Entitat> entitats;

    // ◄ NUEVA RELACIÓN: Enlace bidireccional con Contacte
    // El "mappedBy" debe llamarse exactamente igual que la propiedad de tipo List<Categoria> que tengas en tu clase Contacte.java (seguramente "categorias")
    @ManyToMany(mappedBy = "categorias")
    private List<Contacte> contactes;

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

    // Geters y Seters de las nuevas relaciones para que Jmix y las pantallas puedan leerlas:

    public List<Entitat> getEntitats() {
        return entitats;
    }

    public void setEntitats(List<Entitat> entitats) {
        this.entitats = entitats;
    }

    public List<Contacte> getContactes() {
        return contactes;
    }

    public void setContactes(List<Contacte> contactes) {
        this.contactes = contactes;
    }
}