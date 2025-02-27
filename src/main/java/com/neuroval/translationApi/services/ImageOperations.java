package com.neuroval.translationApi.services;

import lombok.Data;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class ImageOperations {

    public String extractTextFromImage(MultipartFile multipartFile, String languageCode) throws IOException, TesseractException {

        // Convert MultipartFile to File
        File tempFile = convertMultipartFileToFile(multipartFile);

        // Initialize Tesseracts
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata"); // Set Tesseract data path
        tesseract.setLanguage(languageCode); // Set language

        // Perform OCR
        String extractedText = tesseract.doOCR(tempFile);

        // Delete temp file after processing
        tempFile.delete();

        return extractedText;
    }

    private File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File tempFile = File.createTempFile("uploaded", ".png");

        // Ensure binary copy
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(file.getBytes());
        }

        return tempFile;
    }

}
