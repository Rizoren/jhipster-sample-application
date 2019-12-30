package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Subsistence;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Subsistence entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubsistenceRepository extends JpaRepository<Subsistence, Long>, JpaSpecificationExecutor<Subsistence> {

}
