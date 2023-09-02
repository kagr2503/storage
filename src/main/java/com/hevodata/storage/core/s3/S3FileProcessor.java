package com.hevodata.storage.core.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.sqs.AmazonSQS;
import com.hevodata.storage.config.AwsResources;
import com.hevodata.storage.core.elastic.ElasticSearchRepository;
import com.hevodata.storage.core.elastic.ElasticSearchRepositoryImpl;
import com.hevodata.storage.core.files.FileProcessorFactory;
import com.hevodata.storage.core.sqs.SqsMessageProcessor;
import com.hevodata.storage.model.SqsMessageEvent;
import com.hevodata.storage.model.StoreFile;
import com.hevodata.storage.util.ExceptionPrint;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

public class S3FileProcessor {

    private static final ElasticSearchRepository<StoreFile> elasticSearchRepo = new ElasticSearchRepositoryImpl();
    private static final String bucketName = "hevo-data";

    @Async
    public static void processS3Files(SqsMessageEvent messageEvent) {

        try {
            if(messageEvent.getEventName().contains("ObjectCreated:")) {
                S3Object s3Object = AwsResources.getInstance().getAmazonS3().getObject(bucketName, messageEvent.getObjectKey());
                if(s3Object == null) return;
                StoreFile storeFile = FileProcessorFactory.createFileProcessor(messageEvent.getObjectKey()).process(s3Object);
                elasticSearchRepo.createOrUpdateDocument(storeFile);
            } else if(messageEvent.getEventName().contains("ObjectRemoved:")) {
                elasticSearchRepo.deleteDocumentById(messageEvent.getObjectKey());
            }
        }catch (Exception e){
            ExceptionPrint.perform(e);
        }

    }
}
