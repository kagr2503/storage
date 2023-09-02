package com.hevodata.storage.model;

public class SqsMessageEvent {
    private String eventName;
    private String bucketName;
    private String objectKey;

    public SqsMessageEvent(String eventName, String bucketName, String objectKey) {
        this.eventName = eventName;
        this.bucketName = bucketName;
        this.objectKey = objectKey;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }
}
