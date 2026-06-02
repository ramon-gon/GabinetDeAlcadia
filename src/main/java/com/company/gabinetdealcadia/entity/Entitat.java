package com.company.gabinetdealcadia.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@JmixEntity
@Table(name = "entitat", indexes = {
        @Index(name = "idx_entitat_nom", columnList = "nom")
})
@Entity
public class Entitat {

    @Id
    @Column(name = "id_entitat", nullable = false)
    @JmixGeneratedValue
    private UUID id;

    @InstanceName
    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "categoria")
    private String categoria;

    @Column(name = "adreça_seu")
    private String adreçaSeu;

    @Column(name = "codi_postal", length = 20)
    private String codiPostal;

    @Column(name = "poblacio")
    private String poblacio;

    @Column(name = "pais")
    private String pais;

    @Column(name = "email_general")
    private String emailGeneral;

    @Column(name = "telefon_seu")
    private String telefonSeu;

    @Column(name = "activa")
    private Boolean activa = true;

    @Version
    @Column(name = "version")
    private Integer version = 1;

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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getAdreçaSeu() {
        return adreçaSeu;
    }

    public void setAdreçaSeu(String adreçaSeu) {
        this.adreçaSeu = adreçaSeu;
    }

    public String getCodiPostal() {
        return codiPostal;
    }

    public void setCodiPostal(String codiPostal) {
        this.codiPostal = codiPostal;
    }

    public String getPoblacio() {
        return poblacio;
    }

    public void setPoblacio(String poblacio) {
        this.poblacio = poblacio;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getEmailGeneral() {
        return emailGeneral;
    }

    public void setEmailGeneral(String emailGeneral) {
        this.emailGeneral = emailGeneral;
    }

    public String getTelefonSeu() {
        return telefonSeu;
    }

    public void setTelefonSeu(String telefonSeu) {
        this.telefonSeu = telefonSeu;
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
