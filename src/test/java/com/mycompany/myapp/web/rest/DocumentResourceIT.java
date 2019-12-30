package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterSampleApplicationApp;
import com.mycompany.myapp.domain.Document;
import com.mycompany.myapp.repository.DocumentRepository;
import com.mycompany.myapp.repository.search.DocumentSearchRepository;
import com.mycompany.myapp.service.DocumentService;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;
import com.mycompany.myapp.service.dto.DocumentCriteria;
import com.mycompany.myapp.service.DocumentQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link DocumentResource} REST controller.
 */
@SpringBootTest(classes = JhipsterSampleApplicationApp.class)
public class DocumentResourceIT {

    private static final String DEFAULT_DOC_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DOC_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_DOC_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DOC_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final byte[] DEFAULT_DOC_BLOB = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_DOC_BLOB = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_DOC_BLOB_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DOC_BLOB_CONTENT_TYPE = "image/png";

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentService documentService;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.DocumentSearchRepositoryMockConfiguration
     */
    @Autowired
    private DocumentSearchRepository mockDocumentSearchRepository;

    @Autowired
    private DocumentQueryService documentQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restDocumentMockMvc;

    private Document document;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DocumentResource documentResource = new DocumentResource(documentService, documentQueryService);
        this.restDocumentMockMvc = MockMvcBuilders.standaloneSetup(documentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Document createEntity(EntityManager em) {
        Document document = new Document()
            .docName(DEFAULT_DOC_NAME)
            .docDate(DEFAULT_DOC_DATE)
            .docBlob(DEFAULT_DOC_BLOB)
            .docBlobContentType(DEFAULT_DOC_BLOB_CONTENT_TYPE);
        return document;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Document createUpdatedEntity(EntityManager em) {
        Document document = new Document()
            .docName(UPDATED_DOC_NAME)
            .docDate(UPDATED_DOC_DATE)
            .docBlob(UPDATED_DOC_BLOB)
            .docBlobContentType(UPDATED_DOC_BLOB_CONTENT_TYPE);
        return document;
    }

    @BeforeEach
    public void initTest() {
        document = createEntity(em);
    }

    @Test
    @Transactional
    public void createDocument() throws Exception {
        int databaseSizeBeforeCreate = documentRepository.findAll().size();

        // Create the Document
        restDocumentMockMvc.perform(post("/api/documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(document)))
            .andExpect(status().isCreated());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeCreate + 1);
        Document testDocument = documentList.get(documentList.size() - 1);
        assertThat(testDocument.getDocName()).isEqualTo(DEFAULT_DOC_NAME);
        assertThat(testDocument.getDocDate()).isEqualTo(DEFAULT_DOC_DATE);
        assertThat(testDocument.getDocBlob()).isEqualTo(DEFAULT_DOC_BLOB);
        assertThat(testDocument.getDocBlobContentType()).isEqualTo(DEFAULT_DOC_BLOB_CONTENT_TYPE);

        // Validate the Document in Elasticsearch
        verify(mockDocumentSearchRepository, times(1)).save(testDocument);
    }

    @Test
    @Transactional
    public void createDocumentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = documentRepository.findAll().size();

        // Create the Document with an existing ID
        document.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentMockMvc.perform(post("/api/documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(document)))
            .andExpect(status().isBadRequest());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeCreate);

        // Validate the Document in Elasticsearch
        verify(mockDocumentSearchRepository, times(0)).save(document);
    }


    @Test
    @Transactional
    public void checkDocNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentRepository.findAll().size();
        // set the field null
        document.setDocName(null);

        // Create the Document, which fails.

        restDocumentMockMvc.perform(post("/api/documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(document)))
            .andExpect(status().isBadRequest());

        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDocuments() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList
        restDocumentMockMvc.perform(get("/api/documents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(document.getId().intValue())))
            .andExpect(jsonPath("$.[*].docName").value(hasItem(DEFAULT_DOC_NAME)))
            .andExpect(jsonPath("$.[*].docDate").value(hasItem(DEFAULT_DOC_DATE.toString())))
            .andExpect(jsonPath("$.[*].docBlobContentType").value(hasItem(DEFAULT_DOC_BLOB_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].docBlob").value(hasItem(Base64Utils.encodeToString(DEFAULT_DOC_BLOB))));
    }
    
    @Test
    @Transactional
    public void getDocument() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get the document
        restDocumentMockMvc.perform(get("/api/documents/{id}", document.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(document.getId().intValue()))
            .andExpect(jsonPath("$.docName").value(DEFAULT_DOC_NAME))
            .andExpect(jsonPath("$.docDate").value(DEFAULT_DOC_DATE.toString()))
            .andExpect(jsonPath("$.docBlobContentType").value(DEFAULT_DOC_BLOB_CONTENT_TYPE))
            .andExpect(jsonPath("$.docBlob").value(Base64Utils.encodeToString(DEFAULT_DOC_BLOB)));
    }


    @Test
    @Transactional
    public void getDocumentsByIdFiltering() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        Long id = document.getId();

        defaultDocumentShouldBeFound("id.equals=" + id);
        defaultDocumentShouldNotBeFound("id.notEquals=" + id);

        defaultDocumentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDocumentShouldNotBeFound("id.greaterThan=" + id);

        defaultDocumentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDocumentShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllDocumentsByDocNameIsEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where docName equals to DEFAULT_DOC_NAME
        defaultDocumentShouldBeFound("docName.equals=" + DEFAULT_DOC_NAME);

        // Get all the documentList where docName equals to UPDATED_DOC_NAME
        defaultDocumentShouldNotBeFound("docName.equals=" + UPDATED_DOC_NAME);
    }

    @Test
    @Transactional
    public void getAllDocumentsByDocNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where docName not equals to DEFAULT_DOC_NAME
        defaultDocumentShouldNotBeFound("docName.notEquals=" + DEFAULT_DOC_NAME);

        // Get all the documentList where docName not equals to UPDATED_DOC_NAME
        defaultDocumentShouldBeFound("docName.notEquals=" + UPDATED_DOC_NAME);
    }

    @Test
    @Transactional
    public void getAllDocumentsByDocNameIsInShouldWork() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where docName in DEFAULT_DOC_NAME or UPDATED_DOC_NAME
        defaultDocumentShouldBeFound("docName.in=" + DEFAULT_DOC_NAME + "," + UPDATED_DOC_NAME);

        // Get all the documentList where docName equals to UPDATED_DOC_NAME
        defaultDocumentShouldNotBeFound("docName.in=" + UPDATED_DOC_NAME);
    }

    @Test
    @Transactional
    public void getAllDocumentsByDocNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where docName is not null
        defaultDocumentShouldBeFound("docName.specified=true");

        // Get all the documentList where docName is null
        defaultDocumentShouldNotBeFound("docName.specified=false");
    }
                @Test
    @Transactional
    public void getAllDocumentsByDocNameContainsSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where docName contains DEFAULT_DOC_NAME
        defaultDocumentShouldBeFound("docName.contains=" + DEFAULT_DOC_NAME);

        // Get all the documentList where docName contains UPDATED_DOC_NAME
        defaultDocumentShouldNotBeFound("docName.contains=" + UPDATED_DOC_NAME);
    }

    @Test
    @Transactional
    public void getAllDocumentsByDocNameNotContainsSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where docName does not contain DEFAULT_DOC_NAME
        defaultDocumentShouldNotBeFound("docName.doesNotContain=" + DEFAULT_DOC_NAME);

        // Get all the documentList where docName does not contain UPDATED_DOC_NAME
        defaultDocumentShouldBeFound("docName.doesNotContain=" + UPDATED_DOC_NAME);
    }


    @Test
    @Transactional
    public void getAllDocumentsByDocDateIsEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where docDate equals to DEFAULT_DOC_DATE
        defaultDocumentShouldBeFound("docDate.equals=" + DEFAULT_DOC_DATE);

        // Get all the documentList where docDate equals to UPDATED_DOC_DATE
        defaultDocumentShouldNotBeFound("docDate.equals=" + UPDATED_DOC_DATE);
    }

    @Test
    @Transactional
    public void getAllDocumentsByDocDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where docDate not equals to DEFAULT_DOC_DATE
        defaultDocumentShouldNotBeFound("docDate.notEquals=" + DEFAULT_DOC_DATE);

        // Get all the documentList where docDate not equals to UPDATED_DOC_DATE
        defaultDocumentShouldBeFound("docDate.notEquals=" + UPDATED_DOC_DATE);
    }

    @Test
    @Transactional
    public void getAllDocumentsByDocDateIsInShouldWork() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where docDate in DEFAULT_DOC_DATE or UPDATED_DOC_DATE
        defaultDocumentShouldBeFound("docDate.in=" + DEFAULT_DOC_DATE + "," + UPDATED_DOC_DATE);

        // Get all the documentList where docDate equals to UPDATED_DOC_DATE
        defaultDocumentShouldNotBeFound("docDate.in=" + UPDATED_DOC_DATE);
    }

    @Test
    @Transactional
    public void getAllDocumentsByDocDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where docDate is not null
        defaultDocumentShouldBeFound("docDate.specified=true");

        // Get all the documentList where docDate is null
        defaultDocumentShouldNotBeFound("docDate.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocumentShouldBeFound(String filter) throws Exception {
        restDocumentMockMvc.perform(get("/api/documents?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(document.getId().intValue())))
            .andExpect(jsonPath("$.[*].docName").value(hasItem(DEFAULT_DOC_NAME)))
            .andExpect(jsonPath("$.[*].docDate").value(hasItem(DEFAULT_DOC_DATE.toString())))
            .andExpect(jsonPath("$.[*].docBlobContentType").value(hasItem(DEFAULT_DOC_BLOB_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].docBlob").value(hasItem(Base64Utils.encodeToString(DEFAULT_DOC_BLOB))));

        // Check, that the count call also returns 1
        restDocumentMockMvc.perform(get("/api/documents/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocumentShouldNotBeFound(String filter) throws Exception {
        restDocumentMockMvc.perform(get("/api/documents?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocumentMockMvc.perform(get("/api/documents/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDocument() throws Exception {
        // Get the document
        restDocumentMockMvc.perform(get("/api/documents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDocument() throws Exception {
        // Initialize the database
        documentService.save(document);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockDocumentSearchRepository);

        int databaseSizeBeforeUpdate = documentRepository.findAll().size();

        // Update the document
        Document updatedDocument = documentRepository.findById(document.getId()).get();
        // Disconnect from session so that the updates on updatedDocument are not directly saved in db
        em.detach(updatedDocument);
        updatedDocument
            .docName(UPDATED_DOC_NAME)
            .docDate(UPDATED_DOC_DATE)
            .docBlob(UPDATED_DOC_BLOB)
            .docBlobContentType(UPDATED_DOC_BLOB_CONTENT_TYPE);

        restDocumentMockMvc.perform(put("/api/documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDocument)))
            .andExpect(status().isOk());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
        Document testDocument = documentList.get(documentList.size() - 1);
        assertThat(testDocument.getDocName()).isEqualTo(UPDATED_DOC_NAME);
        assertThat(testDocument.getDocDate()).isEqualTo(UPDATED_DOC_DATE);
        assertThat(testDocument.getDocBlob()).isEqualTo(UPDATED_DOC_BLOB);
        assertThat(testDocument.getDocBlobContentType()).isEqualTo(UPDATED_DOC_BLOB_CONTENT_TYPE);

        // Validate the Document in Elasticsearch
        verify(mockDocumentSearchRepository, times(1)).save(testDocument);
    }

    @Test
    @Transactional
    public void updateNonExistingDocument() throws Exception {
        int databaseSizeBeforeUpdate = documentRepository.findAll().size();

        // Create the Document

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentMockMvc.perform(put("/api/documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(document)))
            .andExpect(status().isBadRequest());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Document in Elasticsearch
        verify(mockDocumentSearchRepository, times(0)).save(document);
    }

    @Test
    @Transactional
    public void deleteDocument() throws Exception {
        // Initialize the database
        documentService.save(document);

        int databaseSizeBeforeDelete = documentRepository.findAll().size();

        // Delete the document
        restDocumentMockMvc.perform(delete("/api/documents/{id}", document.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Document in Elasticsearch
        verify(mockDocumentSearchRepository, times(1)).deleteById(document.getId());
    }

    @Test
    @Transactional
    public void searchDocument() throws Exception {
        // Initialize the database
        documentService.save(document);
        when(mockDocumentSearchRepository.search(queryStringQuery("id:" + document.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(document), PageRequest.of(0, 1), 1));
        // Search the document
        restDocumentMockMvc.perform(get("/api/_search/documents?query=id:" + document.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(document.getId().intValue())))
            .andExpect(jsonPath("$.[*].docName").value(hasItem(DEFAULT_DOC_NAME)))
            .andExpect(jsonPath("$.[*].docDate").value(hasItem(DEFAULT_DOC_DATE.toString())))
            .andExpect(jsonPath("$.[*].docBlobContentType").value(hasItem(DEFAULT_DOC_BLOB_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].docBlob").value(hasItem(Base64Utils.encodeToString(DEFAULT_DOC_BLOB))));
    }
}
