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
 * Criteria class for the {@link com.mycompany.myapp.domain.Document} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.DocumentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /documents?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DocumentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter docName;

    private InstantFilter docDate;

    public DocumentCriteria(){
    }

    public DocumentCriteria(DocumentCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.docName = other.docName == null ? null : other.docName.copy();
        this.docDate = other.docDate == null ? null : other.docDate.copy();
    }

    @Override
    public DocumentCriteria copy() {
        return new DocumentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDocName() {
        return docName;
    }

    public void setDocName(StringFilter docName) {
        this.docName = docName;
    }

    public InstantFilter getDocDate() {
        return docDate;
    }

    public void setDocDate(InstantFilter docDate) {
        this.docDate = docDate;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DocumentCriteria that = (DocumentCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(docName, that.docName) &&
            Objects.equals(docDate, that.docDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        docName,
        docDate
        );
    }

    @Override
    public String toString() {
        return "DocumentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (docName != null ? "docName=" + docName + ", " : "") +
                (docDate != null ? "docDate=" + docDate + ", " : "") +
            "}";
    }

}
