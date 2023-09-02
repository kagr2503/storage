package com.hevodata.storage.core.files;

import com.amazonaws.services.s3.model.S3Object;
import com.hevodata.storage.model.StoreFile;

public class UnsupportedFileProcessor extends FileProcessor {

    @Override
    public StoreFile process(S3Object s3Object) {
        throw new RuntimeException("Not Supported");
    }
}
