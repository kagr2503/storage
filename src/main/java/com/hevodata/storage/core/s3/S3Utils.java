package com.hevodata.storage.core.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class S3Utils {

    public static List<S3ObjectSummary> listS3Objects(AmazonS3 amazonS3, String bucketName) {

        ListObjectsV2Request listObjectsRequest = new ListObjectsV2Request()
                .withBucketName(bucketName).withStartAfter("");

        ListObjectsV2Result listObjectsResponse = amazonS3.listObjectsV2(listObjectsRequest);
        return listObjectsResponse.getObjectSummaries();
    }


    public static String identifyFileType(String objectKey) {
        // Create a map of file extensions to MIME types
        Set<String> fileExtensionType = new HashSet<>();
        fileExtensionType.add("pdf");
        fileExtensionType.add("txt");
        fileExtensionType.add("jpg");
        fileExtensionType.add("png");
        fileExtensionType.add("csv");
        fileExtensionType.add("doc");
        fileExtensionType.add("docx");
        // Add more mappings as needed

        // Split the objectKey to extract the file extension
        String[] parts = objectKey.split("\\.");
        if (parts.length > 1) {
            String fileExtension = parts[parts.length - 1].toLowerCase();
            // Check if the file extension is in the mapping
            if (fileExtensionType.contains(fileExtension)) {
                return fileExtension;
            }
        }

        // Default to "application/octet-stream" for unknown file types
        return "application/octet-stream";
    }
}
