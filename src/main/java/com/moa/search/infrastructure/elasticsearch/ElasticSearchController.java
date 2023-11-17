package com.moa.search.infrastructure.elasticsearch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/search/es")
public class ElasticSearchController {

    private final ElasticSearchService elasticSearchService;

    @GetMapping("")
    public void handle() throws IOException {
        elasticSearchService.testSearch();
    }


}
