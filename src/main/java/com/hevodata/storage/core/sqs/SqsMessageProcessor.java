package com.hevodata.storage.core.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hevodata.storage.config.AwsResources;
import com.hevodata.storage.model.SqsMessageEvent;
import com.hevodata.storage.util.ExceptionPrint;

import java.util.ArrayList;
import java.util.List;

public class SqsMessageProcessor implements MessageProcessor {

    private static SqsMessageProcessor instance;
    private static final AwsResources awsResources = AwsResources.getInstance();
    private static final String QUEUE_URL = "https://sqs.ap-south-1.amazonaws.com/936065081286/hevo";

    public static SqsMessageProcessor getInstance(){
        if(instance == null) instance = new SqsMessageProcessor();
        return instance;
    }

    @Override
    public void sendMessage(String message) {
        awsResources.getAmazonSQS().sendMessage(QUEUE_URL, message);
    }

    @Override
    public int sizeOfQueue() {
        try {
            GetQueueAttributesRequest attributesRequest = new GetQueueAttributesRequest()
                    .withQueueUrl(QUEUE_URL)
                    .withAttributeNames(QueueAttributeName.ApproximateNumberOfMessages.toString());

            GetQueueAttributesResult attributesResult = awsResources.getAmazonSQS().getQueueAttributes(attributesRequest);
            return Integer.parseInt(attributesResult.getAttributes().get(QueueAttributeName.ApproximateNumberOfMessages.toString()));
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public List<SqsMessageEvent> getMessageForQueue() {
        List<SqsMessageEvent> sqsMessageEventList = new ArrayList<>();

        try {
            ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(QUEUE_URL)
                    .withMaxNumberOfMessages(10)
                    .withWaitTimeSeconds(20);

            ReceiveMessageResult receiveMessageResponse = awsResources.getAmazonSQS().receiveMessage(receiveMessageRequest);

            for (Message message : receiveMessageResponse.getMessages()) {
                // Handle the S3 event message
                String s3EventMessageBody = message.getBody();
                System.out.println("Received S3 event message: " + s3EventMessageBody);

                try {
                    // Parse the JSON string
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode rootNode = objectMapper.readTree(s3EventMessageBody).get("Records");

                    if (rootNode != null) {
                        // Extract information from the JSON
                        JsonNode firstRecord = rootNode.get(0);
                        String eventSource = firstRecord.get("eventSource").asText();
                        String eventName = firstRecord.get("eventName").asText();
                        JsonNode s3 = firstRecord.get("s3");
                        String bucketName = s3.get("bucket").get("name").asText();
                        String objectKey = s3.get("object").get("key").asText();
                        SqsMessageEvent sqsMessageEvent = new SqsMessageEvent(eventName, bucketName, objectKey);
                        sqsMessageEventList.add(sqsMessageEvent);
                    }
                } catch (Exception e) {
                    ExceptionPrint.perform(e);
                }

                // Delete the processed message from the queue
                awsResources.getAmazonSQS().deleteMessage(QUEUE_URL, message.getReceiptHandle());
            }
        } catch (Exception e) {
            ExceptionPrint.perform(e);
        }

        return sqsMessageEventList;
    }

    @Override
    public void deleteMessage(Message message) {
        awsResources.getAmazonSQS().deleteMessage(QUEUE_URL, message.getReceiptHandle());
    }
}
