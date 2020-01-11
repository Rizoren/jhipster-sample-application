package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Subsistence;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;


/**
 * Spring Data  repository for the Subsistence entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubsistenceRepository extends JpaRepository<Subsistence, Long>, JpaSpecificationExecutor<Subsistence> {
    Optional<Subsistence> findFirstByQuarterSLAndYearSLAndRegion_RegionCode(Integer quarter, String year, String regioncode);
    Optional<Subsistence> findFirstByDateAcceptSLIsLessThanEqualAndRegion_RegionCodeOrderByDateAcceptSLDesc(Instant date, String regioncode);
}
