package com.hevodata.storage.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsyncClient;

import java.util.concurrent.atomic.AtomicReference;

public class AwsResources {

    private final String accessKey = "AKIA5T4OTT7DO7ZVYHHM";
    private final String secretKey = "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";

    private static final AtomicReference<AwsResources> INSTANCE = new AtomicReference<>(null);
    private final AmazonS3 amazonS3;
    private final AmazonSQS amazonSQS;

    private AwsResources() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        amazonS3 = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.AP_SOUTH_1) // Change to your desired region
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();

        amazonSQS = new AmazonSQSAsyncClient(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.AP_SOUTH_1);
    }

    public static AwsResources getInstance() {
        if (INSTANCE.get() == null) {
            synchronized (AwsResources.class) {
                if (INSTANCE.get() == null) {
                    INSTANCE.set(new AwsResources());
                }
            }
        }
        return INSTANCE.get();
    }

    public AmazonS3 getAmazonS3() {
        return amazonS3;
    }

    public AmazonSQS getAmazonSQS() {
        return amazonSQS;
    }
}
