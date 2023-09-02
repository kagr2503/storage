package com.hevodata.storage.core.elastic;

import java.io.IOException;
import java.util.List;

public interface ElasticSearchRepository<T> {
    String createOrUpdateDocument(T document) throws IOException;

    T getDocumentById(String id) throws IOException;

    String deleteDocumentById(String id) throws IOException;

    List<T> searchAllDocuments() throws IOException;

    List<T> searchByContent(String text) throws IOException;
}
