package com.hevodata.storage.core.elastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.hevodata.storage.config.ElasticsearchConfig;
import com.hevodata.storage.model.StoreFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class ElasticSearchRepositoryImpl implements ElasticSearchRepository<StoreFile> {

    private final ElasticsearchClient elasticsearchClient = ElasticsearchConfig.getInstance().getClient();

    private final String indexName = "hevo-search";

    @Override
    @Async
    public String createOrUpdateDocument(StoreFile storeFile) throws IOException {
        IndexResponse response = elasticsearchClient.index(i -> i
                .index(indexName)
                .id(storeFile.getName())
                .document(storeFile)
        );

        String message = response.result().name().equals("Created")
                ? "Document has been successfully created."
                : (response.result().name().equals("Updated")
                ? "Document has been successfully updated."
                : "Error while performing the operation.");

        return new StringBuilder(message).toString();
    }

    @Override
    public StoreFile getDocumentById(String fileId) throws IOException {
        GetResponse<StoreFile> response = elasticsearchClient.get(g -> g
                        .index(indexName)
                        .id(fileId),
                StoreFile.class
        );

        if (!response.found()) {
            System.out.println("File not found");
            return null;
        }

        StoreFile file = response.source();
        System.out.println("File name " + file.getName());
        return file;
    }

    @Override
    @Async
    public String deleteDocumentById(String fileName) throws IOException {
        DeleteRequest request = DeleteRequest.of(d -> d.index(indexName).id(fileName));

        DeleteResponse deleteResponse = elasticsearchClient.delete(request);

        if (Objects.nonNull(deleteResponse.result()) && !deleteResponse.result().name().equals("NotFound")) {
            return new StringBuilder("File with id " + deleteResponse.id() + " has been deleted.").toString();
        }

        System.out.println("File not found");
        return new StringBuilder("File with id " + fileName + " does not exist.").toString();
    }

    @Override
    public List<StoreFile> searchAllDocuments() throws IOException {
        SearchRequest searchRequest =  SearchRequest.of(s -> s.index(indexName));
        SearchResponse searchResponse =  elasticsearchClient.search(searchRequest, StoreFile.class);
        List<Hit> hits = searchResponse.hits().hits();
        List<StoreFile> files = new ArrayList<>();

        for (Hit object : hits) {
            System.out.print(((StoreFile) object.source()));
            files.add((StoreFile) object.source());
        }

        return files;
    }

    @Override
    public List<StoreFile> searchByContent(String text) throws IOException {
        SearchRequest searchRequest =  SearchRequest.of(s -> s.index(indexName).query(q ->
                q.match(t -> t.field("content").query(text))));

        SearchResponse<StoreFile> searchResponse =  elasticsearchClient.search(searchRequest,
                StoreFile.class);

        List<Hit<StoreFile>> hits = searchResponse.hits().hits();
        List<StoreFile> files = new ArrayList<>();

        for (Hit<StoreFile> object : hits) {
            System.out.print(object.source().getUrl());
            files.add(object.source());
        }

        return files;
    }
}
