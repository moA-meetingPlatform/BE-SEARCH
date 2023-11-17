package com.moa.search.infrastructure.elasticsearch;


import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestClient {


    @Value("elasticsearch-es-http.elk.svc.cluster.local")
    private String ELASTICSEARCH_HOSTNAME;
    @Value("awdfaf")
    private String ELASTICSEARCH_USERNAME;
    @Value("awdfaf")
    private  String ELASTICSEARCH_PASSWORD;


    @Bean
    public org.elasticsearch.client.RestClient createClient() {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(ELASTICSEARCH_USERNAME, ELASTICSEARCH_PASSWORD));

        RestClientBuilder builder = org.elasticsearch.client.RestClient.builder(
                        new HttpHost(ELASTICSEARCH_HOSTNAME, 9200, "https"))
                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                        return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    }
                });

        return builder.build();
    }
}
