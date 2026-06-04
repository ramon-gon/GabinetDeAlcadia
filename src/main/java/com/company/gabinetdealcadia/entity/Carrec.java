package com.company.gabinetdealcadia.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@JmixEntity
@Table(name = "carrec")
@Entity
public class Carrec {

    @Id
    @Column(name = "id_carrec", nullable = false)
    @JmixGeneratedValue
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "id_contacte",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_carrec_contacte")
    )
    private Contacte contacte;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "id_entitat",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_carrec_entitat") // Forzamos el nombre de tu SQL original
    )
    private Entitat entitat;

    @InstanceName
    @Column(name = "titol_carrec")
    private String titolCarrec;

    @Column(name = "vigent")
    private Boolean vigent = true;

    @Column(name = "telefon_directe")
    private String telefonDirecte;

    @Column(name = "email_corporatiu")
    private String emailCorporatiu;

    @Column(name = "punt_trameses")
    private Boolean puntTrameses = true;

    @Lob
    @Column(name = "observacions_carrec")
    private String observacionsCarrec;

    @Version
    @Column(name = "version")
    private Integer version = 1;

    @Column(name = "comentari_telefon")
    private String comentariTelefon;

    public String getComentariTelefon() {
        return comentariTelefon;
    }

    public void setComentariTelefon(String comentariTelefon) {
        this.comentariTelefon = comentariTelefon;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Contacte getContacte() {
        return contacte;
    }

    public void setContacte(Contacte contacte) {
        this.contacte = contacte;
    }

    public Entitat getEntitat() {
        return entitat;
    }

    public void setEntitat(Entitat entitat) {
        this.entitat = entitat;
    }

    public String getTitolCarrec() {
        return titolCarrec;
    }

    public void setTitolCarrec(String titolCarrec) {
        this.titolCarrec = titolCarrec;
    }

    public Boolean getVigent() {
        return vigent;
    }

    public void setVigent(Boolean vigent) {
        this.vigent = vigent;
    }

    public String getTelefonDirecte() {
        return telefonDirecte;
    }

    public void setTelefonDirecte(String telefonDirecte) {
        this.telefonDirecte = telefonDirecte;
    }

    public String getEmailCorporatiu() {
        return emailCorporatiu;
    }

    public void setEmailCorporatiu(String emailCorporatiu) {
        this.emailCorporatiu = emailCorporatiu;
    }

    public Boolean getPuntTrameses() {
        return puntTrameses;
    }

    public void setPuntTrameses(Boolean puntTrameses) {
        this.puntTrameses = puntTrameses;
    }

    public String getObservacionsCarrec() {
        return observacionsCarrec;
    }

    public void setObservacionsCarrec(String observacionsCarrec) {
        this.observacionsCarrec = observacionsCarrec;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}