package com.company.gabinetdealcadia.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@JmixEntity
@Table(name = "contacte", indexes = {
        @Index(name = "idx_contacte_cognoms", columnList = "primer_cognom, nom")
})
@Entity
public class Contacte {

    @Id
    @Column(name = "id_contacte", nullable = false)
    @JmixGeneratedValue
    private UUID id;

    @NotNull
    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    @NotNull
    @Column(name = "primer_cognom", nullable = false, length = 100)
    private String primer_cognom;

    @Column(name = "segon_cognom", length = 100)
    private String segon_cognom;

    @Column(name = "data_naixement")
    private LocalDate dataNaixement;

    @Column(name = "nom_conjuge", length = 100)
    private String nomConjuge;

    @Column(name = "primer_cognom_conjuge", length = 100)
    private String primerCognomConjuge;

    @Column(name = "segon_cognom_conjuge", length = 100)
    private String segonCognomConjuge;

    @Column(name = "sexe", length = 20)
    @Enumerated(EnumType.STRING)
    private Sexe sexe;

    @Column(name = "sexe_conjuge", length = 20)
    @Enumerated(EnumType.STRING)
    private Sexe sexeConjuge;

    @Column(name = "email_personal")
    private String emailPersonal;

    @Column(name = "telefon_mobil", length = 50)
    private String telefonMobil;

    @Column(name = "adreça_particular")
    private String adreçaParticular;

    @Column(name = "codi_postal_particular", length = 20)
    private String codiPostalParticular;

    @Column(name = "poblacio_particular")
    private String poblacioParticular;

    @Lob
    @Column(name = "observacions_historiques")
    private String observacionsHistoriques;

    @Version
    @Column(name = "version")
    private Integer version = 1;

    // Nombre de instancia Jmix combinando Nombre y Apellido
    @InstanceName
    public String getDisplayName() {
        return String.format("%s %s", nom != null ? nom : "", primer_cognom != null ? primer_cognom : "").trim();
    }

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

    public String getPrimer_cognom() {
        return primer_cognom;
    }

    public void setPrimer_cognom(String primer_cognom) {
        this.primer_cognom = primer_cognom;
    }

    public String getSegon_cognom() {
        return segon_cognom;
    }

    public void setSegon_cognom(String segon_cognom) {
        this.segon_cognom = segon_cognom;
    }


    public LocalDate getDataNaixement() {
        return dataNaixement;
    }

    public void setDataNaixement(LocalDate dataNaixement) {
        this.dataNaixement = dataNaixement;
    }

    public String getNomConjuge() {
        return nomConjuge;
    }

    public void setNomConjuge(String nomConjuge) {
        this.nomConjuge = nomConjuge;
    }

    public String getPrimerCognomConjuge() {
        return primerCognomConjuge;
    }

    public void setPrimerCognomConjuge(String primerCognomConjuge) {
        this.primerCognomConjuge = primerCognomConjuge;
    }

    public String getSegonCognomConjuge() {
        return segonCognomConjuge;
    }

    public void setSegonCognomConjuge(String segonCognomConjuge) {
        this.segonCognomConjuge = segonCognomConjuge;
    }

    public Sexe getSexe() {
        return sexe;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public Sexe getSexeConjuge() {
        return sexeConjuge;
    }

    public void setSexeConjuge(Sexe sexeConjuge) {
        this.sexeConjuge = sexeConjuge;
    }

    public String getEmailPersonal() {
        return emailPersonal;
    }

    public void setEmailPersonal(String emailPersonal) {
        this.emailPersonal = emailPersonal;
    }

    public String getTelefonMobil() {
        return telefonMobil;
    }

    public void setTelefonMobil(String telefonMobil) {
        this.telefonMobil = telefonMobil;
    }

    public String getAdreçaParticular() {
        return adreçaParticular;
    }

    public void setAdreçaParticular(String adreçaParticular) {
        this.adreçaParticular = adreçaParticular;
    }

    public String getCodiPostalParticular() {
        return codiPostalParticular;
    }

    public void setCodiPostalParticular(String codiPostalParticular) {
        this.codiPostalParticular = codiPostalParticular;
    }

    public String getPoblacioParticular() {
        return poblacioParticular;
    }

    public void setPoblacioParticular(String poblacioParticular) {
        this.poblacioParticular = poblacioParticular;
    }

    public String getObservacionsHistoriques() {
        return observacionsHistoriques;
    }

    public void setObservacionsHistoriques(String observacionsHistoriques) {
        this.observacionsHistoriques = observacionsHistoriques;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}