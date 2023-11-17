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
import org.springframework.core.io.Resource;
import javax.net.ssl.SSLContext;
import org.apache.http.ssl.SSLContextBuilder;

@Configuration
public class RestClient {

    @Value("elasticsearch-es-http.elk.svc.cluster.local")
    private String ELASTICSEARCH_HOSTNAME;
    @Value("awdfaf")
    private String ELASTICSEARCH_USERNAME;
    @Value("awdfaf")
    private String ELASTICSEARCH_PASSWORD;
    @Value("file:/mnt/elastic-internal/elasticsearch-association/elk/elasticsearch/certs/ca.crt")
    private Resource caCertificate;

    @Bean
    public org.elasticsearch.client.RestClient createClient() {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(ELASTICSEARCH_USERNAME, ELASTICSEARCH_PASSWORD));

        RestClientBuilder builder = org.elasticsearch.client.RestClient.builder(
                        new HttpHost(ELASTICSEARCH_HOSTNAME, 9200, "https"))
                .setHttpClientConfigCallback(httpClientBuilder -> {
                    try {
                        SSLContext sslContext = SSLContextBuilder.create()
                                .loadTrustMaterial(caCertificate.getFile(), null) // CA 인증서 로드
                                .build();
                        httpClientBuilder.setSSLContext(sslContext);
                    } catch (Exception e) {
                        throw new RuntimeException("SSL 컨텍스트 구성 중 오류 발생", e);
                    }
                    return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                });

        return builder.build();
    }
}
