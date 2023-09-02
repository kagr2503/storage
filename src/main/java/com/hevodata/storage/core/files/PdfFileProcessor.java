package com.hevodata.storage.core.files;

import com.amazonaws.services.s3.model.S3Object;
import com.hevodata.storage.model.StoreFile;
import com.hevodata.storage.util.ExceptionPrint;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;

public class PdfFileProcessor extends FileProcessor {

    @Override
    public StoreFile process(S3Object s3Object) {
        StoreFile storeFile = new StoreFile();
        storeFile.setUrl(getUrl(s3Object.getBucketName(), s3Object.getKey()));
        storeFile.setName(s3Object.getKey());

        try {
            PDDocument document = PDDocument.load(s3Object.getObjectContent());
            PDFTextStripper pdfTextStripper = new PDFTextStripper();

            String pdfContent = pdfTextStripper.getText(document);
            storeFile.setContent(pdfContent);
            document.close();
        } catch (IOException e) {
            ExceptionPrint.perform(e);
        }
        return storeFile;
    }
}
