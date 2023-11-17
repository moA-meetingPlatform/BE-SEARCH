package com.moa.search.infrastructure.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.CrudRepository;

public interface ElasticSearchItemsRepository extends ElasticsearchRepository<ElasticSearchItems, String>, CrudRepository<ElasticSearchItems, String> {

}
