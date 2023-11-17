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
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Configuration
public class RestClient {

    @Value("elasticsearch-es-http.elk.svc.cluster.local")
    private String ELASTICSEARCH_HOSTNAME;
    @Value("awdfaf")
    private String ELASTICSEARCH_USERNAME;
    @Value("awdfaf")
    private String ELASTICSEARCH_PASSWORD;
    @Value("your-certificate-fingerprint") // 인증서 지문 값
    private String sslCertificateFingerprint;

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
                                .loadTrustMaterial((chain, authType) -> {
                                    for (X509Certificate cert : chain) {
                                        try {
                                            if (calculateFingerprint(cert, "SHA-256").equals(sslCertificateFingerprint)) {
                                                return true;
                                            }
                                        } catch (NoSuchAlgorithmException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                    return false;
                                }).build();
                        httpClientBuilder.setSSLContext(sslContext);
                        httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                        return httpClientBuilder;
                    } catch (Exception e) {
                        throw new RuntimeException("SSL 컨텍스트 구성 중 오류 발생", e);
                    }
                });

        return builder.build();
    }

    private static String calculateFingerprint(X509Certificate cert, String hashAlgorithm) throws NoSuchAlgorithmException, CertificateEncodingException {
        MessageDigest md = MessageDigest.getInstance(hashAlgorithm);
        byte[] der = cert.getEncoded();
        md.update(der);
        byte[] digest = md.digest();
        return toHexString(digest);
    }

    private static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }
}
