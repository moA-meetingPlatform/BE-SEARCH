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
import org.apache.http.ssl.SSLContexts;

import java.io.ByteArrayInputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

@Configuration
public class RestClient {

    @Value("elasticsearch-es-http.elk.svc.cluster.local")
    private String ELASTICSEARCH_HOSTNAME;
    @Value("awdfaf")
    private String ELASTICSEARCH_USERNAME;
    @Value("awdfaf")
    private String ELASTICSEARCH_PASSWORD;
    @Value("${ECK_ES_SSL_CERTIFICATE_AUTHORITY}")
    private String sslCertificateBase64;

    @Bean
    public org.elasticsearch.client.RestClient createClient() {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(ELASTICSEARCH_USERNAME, ELASTICSEARCH_PASSWORD));

        RestClientBuilder builder = org.elasticsearch.client.RestClient.builder(
                        new HttpHost(ELASTICSEARCH_HOSTNAME, 9200, "https"))
                .setHttpClientConfigCallback(httpClientBuilder -> {
                    try {
                        CertificateFactory factory = CertificateFactory.getInstance("X.509");
                        byte[] decodedCert = Base64.getDecoder().decode(sslCertificateBase64);
                        X509Certificate cert = (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(decodedCert));

                        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                        trustStore.load(null, null);
                        trustStore.setCertificateEntry("ca", cert);

                        SSLContextBuilder sslBuilder = SSLContexts.custom()
                                .loadTrustMaterial(trustStore, null);
                        SSLContext sslContext = sslBuilder.build();

                        return httpClientBuilder.setSSLContext(sslContext);
                    } catch (Exception e) {
                        throw new RuntimeException("SSL 컨텍스트 구성 중 오류 발생", e);
                    }
                });

        return builder.build();
    }
}
