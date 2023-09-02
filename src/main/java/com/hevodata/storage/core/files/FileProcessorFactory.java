package com.hevodata.storage.core.files;

import com.hevodata.storage.core.s3.S3Utils;

public class FileProcessorFactory {

    public static FileProcessor createFileProcessor(String objectKey) {
        String fileType = S3Utils.identifyFileType(objectKey);

        switch (fileType) {
            case "pdf":
                return new PdfFileProcessor();
            case "csv":
            case "txt":
            case "doc":
            case "docx":
                return new TextFileProcessor();
            case "jpg":
            case "png":
                // Implement image processing here
                return new ImageFileProcessor();
            default:
                return new UnsupportedFileProcessor();
        }
    }
}
