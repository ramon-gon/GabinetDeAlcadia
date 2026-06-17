package com.company.gabinetdealcadia.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.DependsOnProperties;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.JmixProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

    // Como ahora la relación necesita especificar el enlace, añadimos el JoinTable aquí también apuntando a id_categoria
    @JoinTable(name = "categoria_contacte_link",
            joinColumns = @JoinColumn(name = "contacte_id", referencedColumnName = "id_contacte"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id", referencedColumnName = "id_categoria")) // 🛠️ Corregido a id_categoria
    @ManyToMany
    private List<Categoria> categories;

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

    @Column(name = "comentari_telefon_mobil")
    private String comentariTelefonMobil;

    @JoinColumn(name = "carrec_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Carrec carrec;

    public String getComentariTelefonMobil() { return comentariTelefonMobil; }
    public void setComentariTelefonMobil(String comentariTelefonMobil) { this.comentariTelefonMobil = comentariTelefonMobil; }

    public Carrec getCarrec() {
        return carrec;
    }

    public void setCarrec(Carrec carrec) {
        this.carrec = carrec;
    }

    public List<Categoria> getCategories() {
        return categories;
    }

    public void setCategories(List<Categoria> categories) {
        this.categories = categories;
    }

    public void addCategoria(Categoria categoria) {
        if (this.categories == null) {
            this.categories = new ArrayList<>();
        }
        if (!this.categories.contains(categoria)) {
            this.categories.add(categoria);
            if (categoria.getContactes() == null) {
                categoria.setContactes(new ArrayList<>());
            }
            if (!categoria.getContactes().contains(this)) {
                categoria.getContactes().add(this);
            }
        }
    }

    public void removeCategoria(Categoria categoria) {
        if (this.categories != null && this.categories.contains(categoria)) {
            this.categories.remove(categoria);
            if (categoria.getContactes() != null) {
                categoria.getContactes().remove(this);
            }
        }
    }

    @JmixProperty
    @DependsOnProperties({"nom", "primer_cognom", "segon_cognom"})
    public String getNomComplet() {
        String n = nom != null ? nom : "";
        String p = primer_cognom != null ? primer_cognom : "";
        String s = segon_cognom != null ? segon_cognom : "";

        return String.format("%s %s %s", n, p, s).trim().replaceAll("\\s+", " ");
    }

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