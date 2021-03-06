package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Subsistence;
import com.mycompany.myapp.service.SubsistenceService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.SubsistenceCriteria;
import com.mycompany.myapp.service.SubsistenceQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Subsistence}.
 */
@RestController
@RequestMapping("/api")
public class SubsistenceResource {

    private final Logger log = LoggerFactory.getLogger(SubsistenceResource.class);

    private static final String ENTITY_NAME = "subsistence";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubsistenceService subsistenceService;

    private final SubsistenceQueryService subsistenceQueryService;

    public SubsistenceResource(SubsistenceService subsistenceService, SubsistenceQueryService subsistenceQueryService) {
        this.subsistenceService = subsistenceService;
        this.subsistenceQueryService = subsistenceQueryService;
    }

    /**
     * {@code POST  /subsistences} : Create a new subsistence.
     *
     * @param subsistence the subsistence to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subsistence, or with status {@code 400 (Bad Request)} if the subsistence has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/subsistences")
    public ResponseEntity<Subsistence> createSubsistence(@Valid @RequestBody Subsistence subsistence) throws URISyntaxException {
        log.debug("REST request to save Subsistence : {}", subsistence);
        if (subsistence.getId() != null) {
            throw new BadRequestAlertException("A new subsistence cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Subsistence result = subsistenceService.save(subsistence);
        return ResponseEntity.created(new URI("/api/subsistences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /subsistences} : Updates an existing subsistence.
     *
     * @param subsistence the subsistence to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subsistence,
     * or with status {@code 400 (Bad Request)} if the subsistence is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subsistence couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/subsistences")
    public ResponseEntity<Subsistence> updateSubsistence(@Valid @RequestBody Subsistence subsistence) throws URISyntaxException {
        log.debug("REST request to update Subsistence : {}", subsistence);
        if (subsistence.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Subsistence result = subsistenceService.save(subsistence);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subsistence.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /subsistences} : get all the subsistences.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subsistences in body.
     */
    @GetMapping("/subsistences")
    public ResponseEntity<List<Subsistence>> getAllSubsistences(SubsistenceCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Subsistences by criteria: {}", criteria);
        Page<Subsistence> page = subsistenceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /subsistences/count} : count all the subsistences.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/subsistences/count")
    public ResponseEntity<Long> countSubsistences(SubsistenceCriteria criteria) {
        log.debug("REST request to count Subsistences by criteria: {}", criteria);
        return ResponseEntity.ok().body(subsistenceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /subsistences/:id} : get the "id" subsistence.
     *
     * @param id the id of the subsistence to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subsistence, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/subsistences/{id}")
    public ResponseEntity<Subsistence> getSubsistence(@PathVariable Long id) {
        log.debug("REST request to get Subsistence : {}", id);
        Optional<Subsistence> subsistence = subsistenceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subsistence);
    }

    /**
     * {@code DELETE  /subsistences/:id} : delete the "id" subsistence.
     *
     * @param id the id of the subsistence to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/subsistences/{id}")
    public ResponseEntity<Void> deleteSubsistence(@PathVariable Long id) {
        log.debug("REST request to delete Subsistence : {}", id);
        subsistenceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/subsistences?query=:query} : search for the subsistence corresponding
     * to the query.
     *
     * @param query the query of the subsistence search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/subsistences")
    public ResponseEntity<List<Subsistence>> searchSubsistences(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Subsistences for query {}", query);
        Page<Subsistence> page = subsistenceService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
