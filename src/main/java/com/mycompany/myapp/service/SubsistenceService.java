package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Subsistence;
import com.mycompany.myapp.repository.SubsistenceRepository;
import com.mycompany.myapp.repository.search.SubsistenceSearchRepository;
import com.mycompany.myapp.service.dto.SubsistenceCriteria;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.StringFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Subsistence}.
 */
@Service
@Transactional
public class SubsistenceService {

    private final Logger log = LoggerFactory.getLogger(SubsistenceService.class);

    private final SubsistenceRepository subsistenceRepository;

    private final SubsistenceSearchRepository subsistenceSearchRepository;

    public SubsistenceService(SubsistenceRepository subsistenceRepository, SubsistenceSearchRepository subsistenceSearchRepository) {
        this.subsistenceRepository = subsistenceRepository;
        this.subsistenceSearchRepository = subsistenceSearchRepository;
    }

    /**
     * Save a subsistence.
     *
     * @param subsistence the entity to save.
     * @return the persisted entity.
     */
    public Subsistence save(Subsistence subsistence) {
        log.debug("Request to save Subsistence : {}", subsistence);
        Subsistence result = subsistenceRepository.save(subsistence);
        subsistenceSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the subsistences.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Subsistence> findAll(Pageable pageable) {
        log.debug("Request to get all Subsistences");
        return subsistenceRepository.findAll(pageable);
    }


    /**
     * Get one subsistence by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Subsistence> findOne(Long id) {
        log.debug("Request to get Subsistence : {}", id);
        return subsistenceRepository.findById(id);
    }

    /**
     * Delete the subsistence by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Subsistence : {}", id);
        subsistenceRepository.deleteById(id);
        subsistenceSearchRepository.deleteById(id);
    }

    /**
     * Search for the subsistence corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Subsistence> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Subsistences for query {}", query);
        return subsistenceSearchRepository.search(queryStringQuery(query), pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Subsistence> findOneByQYRC(String q, String y, String rc) {
        log.debug("Request to get Subsistence : {}", q, y, rc);
        Optional<Subsistence> s = subsistenceRepository.findFirstByQuarterSLAndYearSLAndRegion_RegionCode(Integer.parseInt(q), y, rc);
        return s;
    }
}
