package com.mycompany.myapp.domain;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;

/**
 * The Document entity.
 */
@ApiModel(description = "The Document entity.")
@Entity
@Table(name = "document")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "document")
public class Document implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "doc_name", length = 255, nullable = false)
    private String docName;

    @Column(name = "doc_date")
    private Instant docDate;

    @Lob
    @Column(name = "doc_blob")
    private byte[] docBlob;

    @Column(name = "doc_blob_content_type")
    private String docBlobContentType;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocName() {
        return docName;
    }

    public Document docName(String docName) {
        this.docName = docName;
        return this;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public Instant getDocDate() {
        return docDate;
    }

    public Document docDate(Instant docDate) {
        this.docDate = docDate;
        return this;
    }

    public void setDocDate(Instant docDate) {
        this.docDate = docDate;
    }

    public byte[] getDocBlob() {
        return docBlob;
    }

    public Document docBlob(byte[] docBlob) {
        this.docBlob = docBlob;
        return this;
    }

    public void setDocBlob(byte[] docBlob) {
        this.docBlob = docBlob;
    }

    public String getDocBlobContentType() {
        return docBlobContentType;
    }

    public Document docBlobContentType(String docBlobContentType) {
        this.docBlobContentType = docBlobContentType;
        return this;
    }

    public void setDocBlobContentType(String docBlobContentType) {
        this.docBlobContentType = docBlobContentType;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Document)) {
            return false;
        }
        return id != null && id.equals(((Document) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Document{" +
            "id=" + getId() +
            ", docName='" + getDocName() + "'" +
            ", docDate='" + getDocDate() + "'" +
            ", docBlob='" + getDocBlob() + "'" +
            ", docBlobContentType='" + getDocBlobContentType() + "'" +
            "}";
    }
}
