package com.neuroval.translationApi.services;

import com.neuroval.translationApi.model.image.Image;
import lombok.Data;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ImageOperations {

    // Extract text from the image using tesseract
    public String extractTextFromImage(MultipartFile multipartFile, String languageCode, Image image) throws IOException, TesseractException {
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

        // Map the extracted text to Java object
        mapper(extractedText, image);

        // Split the words
        splitWords(extractedText, image);

        return extractedText;
    }

    // Convert multiple part file to single file
    private File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File tempFile = File.createTempFile("uploaded", ".png");

        // Ensure binary copy
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(file.getBytes());
        }

        return tempFile;
    }

    // Map the extracted text to Image JAVA object
    public String mapper(String text, Image image){
        image.setText(text);
        return image.getText();
    }

    // Split the words from entire string and map them to a List
    public List<String> splitWords(String text, Image image){
        // Split the string by spaces
        String[] wordsArray = text.split("\\s+");

        // Convert array to ArrayList
        List<String> wordsList = new ArrayList<>(Arrays.asList(wordsArray));

        // Set image word list
        image.setTextList(wordsList);

        return wordsList;
    }



}
