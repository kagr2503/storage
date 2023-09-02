package com.hevodata.storage.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.sqs.AmazonSQS;
import com.hevodata.storage.core.elastic.ElasticSearchRepository;
import com.hevodata.storage.core.s3.S3FileProcessor;
import com.hevodata.storage.model.StoreFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ElasticSearchController {

    private final ElasticSearchRepository<StoreFile> elasticsearchRepository;

    @Autowired
    public ElasticSearchController(ElasticSearchRepository<StoreFile> elasticsearchRepository) {
        this.elasticsearchRepository = elasticsearchRepository;
    }

    @PostMapping("/documents")
    public ResponseEntity<String> createOrUpdateDocument(@RequestBody StoreFile file) throws IOException {
        String response = elasticsearchRepository.createOrUpdateDocument(file);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/documents/{id}")
    public ResponseEntity<StoreFile> getDocumentById(@PathVariable String id) throws IOException {
        StoreFile document = elasticsearchRepository.getDocumentById(id);
        return document != null
                ? new ResponseEntity<>(document, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/documents/{id}")
    public ResponseEntity<String> deleteDocumentById(@PathVariable String id) throws IOException {
        String response = elasticsearchRepository.deleteDocumentById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/documents/search")
    public ResponseEntity<List<StoreFile>> searchAllDocuments() throws IOException {
        List<StoreFile> documents = elasticsearchRepository.searchAllDocuments();
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

    @GetMapping("/documents/searchByContent")
    public ResponseEntity<List<StoreFile>> searchByContent(@RequestParam String text) throws IOException {
        List<StoreFile> documents = elasticsearchRepository.searchByContent(text);
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }
}
