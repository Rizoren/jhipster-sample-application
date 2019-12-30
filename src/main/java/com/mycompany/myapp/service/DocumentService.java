package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Document;
import com.mycompany.myapp.repository.DocumentRepository;
import com.mycompany.myapp.repository.search.DocumentSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Document}.
 */
@Service
@Transactional
public class DocumentService {

    private final Logger log = LoggerFactory.getLogger(DocumentService.class);

    private final DocumentRepository documentRepository;

    private final DocumentSearchRepository documentSearchRepository;

    public DocumentService(DocumentRepository documentRepository, DocumentSearchRepository documentSearchRepository) {
        this.documentRepository = documentRepository;
        this.documentSearchRepository = documentSearchRepository;
    }

    /**
     * Save a document.
     *
     * @param document the entity to save.
     * @return the persisted entity.
     */
    public Document save(Document document) {
        log.debug("Request to save Document : {}", document);
        Document result = documentRepository.save(document);
        documentSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the documents.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Document> findAll() {
        log.debug("Request to get all Documents");
        return documentRepository.findAll();
    }


    /**
     * Get one document by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Document> findOne(Long id) {
        log.debug("Request to get Document : {}", id);
        return documentRepository.findById(id);
    }

    /**
     * Delete the document by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Document : {}", id);
        documentRepository.deleteById(id);
        documentSearchRepository.deleteById(id);
    }

    /**
     * Search for the document corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Document> search(String query) {
        log.debug("Request to search Documents for query {}", query);
        return StreamSupport
            .stream(documentSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
