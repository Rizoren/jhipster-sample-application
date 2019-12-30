package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Subsistence} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.SubsistenceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /subsistences?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SubsistenceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter yearSL;

    private IntegerFilter quarterSL;

    private InstantFilter dateAcceptSL;

    private DoubleFilter valuePerCapitaSL;

    private DoubleFilter valueForCapableSL;

    private DoubleFilter valueForPensionersSL;

    private DoubleFilter valueForChildrenSL;

    private LongFilter docId;

    private LongFilter regionId;

    public SubsistenceCriteria(){
    }

    public SubsistenceCriteria(SubsistenceCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.yearSL = other.yearSL == null ? null : other.yearSL.copy();
        this.quarterSL = other.quarterSL == null ? null : other.quarterSL.copy();
        this.dateAcceptSL = other.dateAcceptSL == null ? null : other.dateAcceptSL.copy();
        this.valuePerCapitaSL = other.valuePerCapitaSL == null ? null : other.valuePerCapitaSL.copy();
        this.valueForCapableSL = other.valueForCapableSL == null ? null : other.valueForCapableSL.copy();
        this.valueForPensionersSL = other.valueForPensionersSL == null ? null : other.valueForPensionersSL.copy();
        this.valueForChildrenSL = other.valueForChildrenSL == null ? null : other.valueForChildrenSL.copy();
        this.docId = other.docId == null ? null : other.docId.copy();
        this.regionId = other.regionId == null ? null : other.regionId.copy();
    }

    @Override
    public SubsistenceCriteria copy() {
        return new SubsistenceCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getYearSL() {
        return yearSL;
    }

    public void setYearSL(StringFilter yearSL) {
        this.yearSL = yearSL;
    }

    public IntegerFilter getQuarterSL() {
        return quarterSL;
    }

    public void setQuarterSL(IntegerFilter quarterSL) {
        this.quarterSL = quarterSL;
    }

    public InstantFilter getDateAcceptSL() {
        return dateAcceptSL;
    }

    public void setDateAcceptSL(InstantFilter dateAcceptSL) {
        this.dateAcceptSL = dateAcceptSL;
    }

    public DoubleFilter getValuePerCapitaSL() {
        return valuePerCapitaSL;
    }

    public void setValuePerCapitaSL(DoubleFilter valuePerCapitaSL) {
        this.valuePerCapitaSL = valuePerCapitaSL;
    }

    public DoubleFilter getValueForCapableSL() {
        return valueForCapableSL;
    }

    public void setValueForCapableSL(DoubleFilter valueForCapableSL) {
        this.valueForCapableSL = valueForCapableSL;
    }

    public DoubleFilter getValueForPensionersSL() {
        return valueForPensionersSL;
    }

    public void setValueForPensionersSL(DoubleFilter valueForPensionersSL) {
        this.valueForPensionersSL = valueForPensionersSL;
    }

    public DoubleFilter getValueForChildrenSL() {
        return valueForChildrenSL;
    }

    public void setValueForChildrenSL(DoubleFilter valueForChildrenSL) {
        this.valueForChildrenSL = valueForChildrenSL;
    }

    public LongFilter getDocId() {
        return docId;
    }

    public void setDocId(LongFilter docId) {
        this.docId = docId;
    }

    public LongFilter getRegionId() {
        return regionId;
    }

    public void setRegionId(LongFilter regionId) {
        this.regionId = regionId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SubsistenceCriteria that = (SubsistenceCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(yearSL, that.yearSL) &&
            Objects.equals(quarterSL, that.quarterSL) &&
            Objects.equals(dateAcceptSL, that.dateAcceptSL) &&
            Objects.equals(valuePerCapitaSL, that.valuePerCapitaSL) &&
            Objects.equals(valueForCapableSL, that.valueForCapableSL) &&
            Objects.equals(valueForPensionersSL, that.valueForPensionersSL) &&
            Objects.equals(valueForChildrenSL, that.valueForChildrenSL) &&
            Objects.equals(docId, that.docId) &&
            Objects.equals(regionId, that.regionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        yearSL,
        quarterSL,
        dateAcceptSL,
        valuePerCapitaSL,
        valueForCapableSL,
        valueForPensionersSL,
        valueForChildrenSL,
        docId,
        regionId
        );
    }

    @Override
    public String toString() {
        return "SubsistenceCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (yearSL != null ? "yearSL=" + yearSL + ", " : "") +
                (quarterSL != null ? "quarterSL=" + quarterSL + ", " : "") +
                (dateAcceptSL != null ? "dateAcceptSL=" + dateAcceptSL + ", " : "") +
                (valuePerCapitaSL != null ? "valuePerCapitaSL=" + valuePerCapitaSL + ", " : "") +
                (valueForCapableSL != null ? "valueForCapableSL=" + valueForCapableSL + ", " : "") +
                (valueForPensionersSL != null ? "valueForPensionersSL=" + valueForPensionersSL + ", " : "") +
                (valueForChildrenSL != null ? "valueForChildrenSL=" + valueForChildrenSL + ", " : "") +
                (docId != null ? "docId=" + docId + ", " : "") +
                (regionId != null ? "regionId=" + regionId + ", " : "") +
            "}";
    }

}
