package com.moa.search.infrastructure.elasticsearch;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ElasticSearchService {


    private final ElasticSearchItemsRepository elasticSearchItemsRepository;

    // search 인덱스에서 검색어를 통해 검색
    public Iterable<ElasticSearchItems> getUserById() {
        return elasticSearchItemsRepository.findAll();
    }

}
