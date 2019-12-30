package com.mycompany.myapp.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;

/**
 * The Subsistence entity.
 */
@ApiModel(description = "The Subsistence entity.")
@Entity
@Table(name = "subsistence")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "subsistence")
public class Subsistence implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "year_sl")
    private Integer yearSL;

    @Column(name = "quarter_sl")
    private Integer quarterSL;

    @Column(name = "date_accept_sl")
    private Instant dateAcceptSL;

    @Column(name = "value_per_capita_sl")
    private Double valuePerCapitaSL;

    @Column(name = "value_for_capable_sl")
    private Double valueForCapableSL;

    @Column(name = "value_for_pensioners_sl")
    private Double valueForPensionersSL;

    @Column(name = "value_for_children_sl")
    private Double valueForChildrenSL;

    @OneToOne
    @JoinColumn(unique = true)
    private Document document;

    @ManyToOne
    @JsonIgnoreProperties("subsistences")
    private Region region;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYearSL() {
        return yearSL;
    }

    public Subsistence yearSL(Integer yearSL) {
        this.yearSL = yearSL;
        return this;
    }

    public void setYearSL(Integer yearSL) {
        this.yearSL = yearSL;
    }

    public Integer getQuarterSL() {
        return quarterSL;
    }

    public Subsistence quarterSL(Integer quarterSL) {
        this.quarterSL = quarterSL;
        return this;
    }

    public void setQuarterSL(Integer quarterSL) {
        this.quarterSL = quarterSL;
    }

    public Instant getDateAcceptSL() {
        return dateAcceptSL;
    }

    public Subsistence dateAcceptSL(Instant dateAcceptSL) {
        this.dateAcceptSL = dateAcceptSL;
        return this;
    }

    public void setDateAcceptSL(Instant dateAcceptSL) {
        this.dateAcceptSL = dateAcceptSL;
    }

    public Double getValuePerCapitaSL() {
        return valuePerCapitaSL;
    }

    public Subsistence valuePerCapitaSL(Double valuePerCapitaSL) {
        this.valuePerCapitaSL = valuePerCapitaSL;
        return this;
    }

    public void setValuePerCapitaSL(Double valuePerCapitaSL) {
        this.valuePerCapitaSL = valuePerCapitaSL;
    }

    public Double getValueForCapableSL() {
        return valueForCapableSL;
    }

    public Subsistence valueForCapableSL(Double valueForCapableSL) {
        this.valueForCapableSL = valueForCapableSL;
        return this;
    }

    public void setValueForCapableSL(Double valueForCapableSL) {
        this.valueForCapableSL = valueForCapableSL;
    }

    public Double getValueForPensionersSL() {
        return valueForPensionersSL;
    }

    public Subsistence valueForPensionersSL(Double valueForPensionersSL) {
        this.valueForPensionersSL = valueForPensionersSL;
        return this;
    }

    public void setValueForPensionersSL(Double valueForPensionersSL) {
        this.valueForPensionersSL = valueForPensionersSL;
    }

    public Double getValueForChildrenSL() {
        return valueForChildrenSL;
    }

    public Subsistence valueForChildrenSL(Double valueForChildrenSL) {
        this.valueForChildrenSL = valueForChildrenSL;
        return this;
    }

    public void setValueForChildrenSL(Double valueForChildrenSL) {
        this.valueForChildrenSL = valueForChildrenSL;
    }

    public Document getDocument() {
        return document;
    }

    public Subsistence document(Document document) {
        this.document = document;
        return this;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public Region getRegion() {
        return region;
    }

    public Subsistence region(Region region) {
        this.region = region;
        return this;
    }

    public void setRegion(Region region) {
        this.region = region;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Subsistence)) {
            return false;
        }
        return id != null && id.equals(((Subsistence) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Subsistence{" +
            "id=" + getId() +
            ", yearSL=" + getYearSL() +
            ", quarterSL=" + getQuarterSL() +
            ", dateAcceptSL='" + getDateAcceptSL() + "'" +
            ", valuePerCapitaSL=" + getValuePerCapitaSL() +
            ", valueForCapableSL=" + getValueForCapableSL() +
            ", valueForPensionersSL=" + getValueForPensionersSL() +
            ", valueForChildrenSL=" + getValueForChildrenSL() +
            "}";
    }
}
