package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Subsistence;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Subsistence} entity.
 */
public interface SubsistenceSearchRepository extends ElasticsearchRepository<Subsistence, Long> {
}
