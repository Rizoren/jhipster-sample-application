package com.mycompany.myapp.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.mycompany.myapp.domain.Subsistence;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.SubsistenceRepository;
import com.mycompany.myapp.repository.search.SubsistenceSearchRepository;
import com.mycompany.myapp.service.dto.SubsistenceCriteria;

/**
 * Service for executing complex queries for {@link Subsistence} entities in the database.
 * The main input is a {@link SubsistenceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Subsistence} or a {@link Page} of {@link Subsistence} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SubsistenceQueryService extends QueryService<Subsistence> {

    private final Logger log = LoggerFactory.getLogger(SubsistenceQueryService.class);

    private final SubsistenceRepository subsistenceRepository;

    private final SubsistenceSearchRepository subsistenceSearchRepository;

    public SubsistenceQueryService(SubsistenceRepository subsistenceRepository, SubsistenceSearchRepository subsistenceSearchRepository) {
        this.subsistenceRepository = subsistenceRepository;
        this.subsistenceSearchRepository = subsistenceSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Subsistence} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Subsistence> findByCriteria(SubsistenceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Subsistence> specification = createSpecification(criteria);
        return subsistenceRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Subsistence} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Subsistence> findByCriteria(SubsistenceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Subsistence> specification = createSpecification(criteria);
        return subsistenceRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SubsistenceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Subsistence> specification = createSpecification(criteria);
        return subsistenceRepository.count(specification);
    }

    /**
     * Function to convert {@link SubsistenceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Subsistence> createSpecification(SubsistenceCriteria criteria) {
        Specification<Subsistence> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Subsistence_.id));
            }
            if (criteria.getYearSL() != null) {
                specification = specification.and(buildStringSpecification(criteria.getYearSL(), Subsistence_.yearSL));
            }
            if (criteria.getQuarterSL() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuarterSL(), Subsistence_.quarterSL));
            }
            if (criteria.getDateAcceptSL() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateAcceptSL(), Subsistence_.dateAcceptSL));
            }
            if (criteria.getValuePerCapitaSL() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValuePerCapitaSL(), Subsistence_.valuePerCapitaSL));
            }
            if (criteria.getValueForCapableSL() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValueForCapableSL(), Subsistence_.valueForCapableSL));
            }
            if (criteria.getValueForPensionersSL() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValueForPensionersSL(), Subsistence_.valueForPensionersSL));
            }
            if (criteria.getValueForChildrenSL() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValueForChildrenSL(), Subsistence_.valueForChildrenSL));
            }
            if (criteria.getDocId() != null) {
                specification = specification.and(buildSpecification(criteria.getDocId(),
                    root -> root.join(Subsistence_.doc, JoinType.LEFT).get(Document_.id)));
            }
            if (criteria.getRegionId() != null) {
                specification = specification.and(buildSpecification(criteria.getRegionId(),
                    root -> root.join(Subsistence_.region, JoinType.LEFT).get(Region_.id)));
            }
        }
        return specification;
    }
}
