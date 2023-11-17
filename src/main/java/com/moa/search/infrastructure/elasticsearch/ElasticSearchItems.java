package com.moa.search.infrastructure.elasticsearch;


import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;


@Document(indexName = "search")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ElasticSearchItems {
    @Id
    private String id;

    private String data;
}
