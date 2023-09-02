package com.hevodata.storage.core.files;

import com.amazonaws.services.s3.model.S3Object;
import com.hevodata.storage.model.StoreFile;
import com.hevodata.storage.util.ExceptionPrint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TextFileProcessor extends FileProcessor {

    @Override
    public StoreFile process(S3Object s3Object) {
        StoreFile storeFile = new StoreFile();
        storeFile.setUrl(getUrl(s3Object.getBucketName(), s3Object.getKey()));
        storeFile.setName(s3Object.getKey());

        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(s3Object.getObjectContent()))) {
            String line;
            System.out.println("File: " + s3Object.getKey());
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            ExceptionPrint.perform(e);
        }
        storeFile.setContent(content.toString());
        return storeFile;
    }

}
