package com.hevodata.storage.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

    private static ElasticsearchConfig instance;
    private final String elasticsearchHost = "localhost";
    private final int elasticsearchPort = 9200;

    public ElasticsearchClient client;

    public static ElasticsearchConfig getInstance(){
        if(instance == null) instance = new ElasticsearchConfig();
        return instance;
    }
    public ElasticsearchConfig(){
        client = new ElasticsearchClient(createElasticsearchTransport(createRestClient()));
    }

    public ElasticsearchClient getClient() {
        return client;
    }

    private RestClient createRestClient() {
        HttpHost httpHost = new HttpHost(elasticsearchHost, elasticsearchPort);
        return RestClient.builder(httpHost).build();
    }

    private ElasticsearchTransport createElasticsearchTransport(RestClient restClient) {
        return new RestClientTransport(restClient, new JacksonJsonpMapper());
    }
}
