package com.moa.search.infrastructure.elasticsearch;


import lombok.RequiredArgsConstructor;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class ElasticSearchService {

    private final RestClient restClient;

    public void testSearch() throws IOException {
        Request request = new Request("GET", "/search/_search");

        Response response = restClient.createClient().performRequest(request);

        String result = EntityUtils.toString(response.getEntity());

        System.out.println(result);
    }


}
