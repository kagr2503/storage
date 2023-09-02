package com.hevodata.storage.core.files;

import com.amazonaws.services.s3.model.S3Object;
import com.hevodata.storage.model.StoreFile;

public abstract class FileProcessor {

    public abstract StoreFile process(S3Object s3Object);
    public static String getUrl(String bucketName, String objectKey) {
            return "https://" + bucketName + ".s3.ap-south-1.amazon.com/" + objectKey;
        }
}
