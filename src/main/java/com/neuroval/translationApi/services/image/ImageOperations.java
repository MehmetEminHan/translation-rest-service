package com.neuroval.translationApi.services.image;

import com.neuroval.translationApi.model.comparison.Comparison;
import com.neuroval.translationApi.model.image.Image;
import com.neuroval.translationApi.repository.ImageRepository;
import com.neuroval.translationApi.services.log.Log;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    Image image;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    private Comparison comparison;

    private static final Logger logger = Log.getLogger(ImageOperations.class);  // Logger initialized for this class only once
    private String extractedText = "";
    private byte[] imageBytes;
    private String fileFormat;




    // Extract text from the image using tesseract
    public String extractTextFromImage(MultipartFile multipartFile, String languageCode) throws IOException, TesseractException {
        // Convert MultipartFile to File
        File tempFile = convertMultipartFileToFile(multipartFile);

        // Get the os name
        String osName = System.getProperty("os.name");
        logger.info("Detected OS Name is: {}", osName);

        // Initialize Tesseracts
        Tesseract tesseract = new Tesseract();

        // Assign tesseract datapath based on OS system
        if(osName.toLowerCase().contains("windows")) {
            tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata"); // Set Tesseract data path windows
        }else if(osName.toLowerCase().contains("linux")) {
            tesseract.setDatapath("/usr/share/tesseract-ocr/5/tessdata"); // Set Tesseract data path ubuntu
        }

        tesseract.setLanguage(languageCode); // Set language
        logger.info("Tesseract language set to {}", languageCode);

        // Perform OCR
        extractedText = tesseract.doOCR(tempFile);
        logger.info("Extracted text from image is: {}", extractedText);

        // Delete temp file after processing
        tempFile.delete();

        // Map the extracted text to Java object
        mapper(extractedText);

        // Split the words
        splitWords(extractedText);

        // Save image bytes
        imageBytes = multipartFile.getBytes();

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
    public String mapper(String text){
        image.setText(text);
        logger.info("Extracted text deserialized to imageText:", image.getText());

        return image.getText();
    }

    // Split the words from entire string and map them to a List
    public List<String> splitWords(String text){
        // Split the string by spaces
        String[] wordsArray = text.split("\\s+");

        // Convert array to ArrayList
        List<String> wordsList = new ArrayList<>(Arrays.asList(wordsArray));

        logger.info("Split words: {}", wordsList);

        // Set image word list
        image.setTextList(wordsList);

        return wordsList;
    }

    // Find the uploaded image file format and return it
    public String getFileFormat(MultipartFile imageFile){
        fileFormat = imageFile.getOriginalFilename().substring(imageFile.getOriginalFilename().lastIndexOf(".")); // Get file format and set to fileFormat;

        return fileFormat;
    }

    // Map image to image entity
    public void mapImageToEntity(){
        image.setText(extractedText); // Set extracted text from image to image object
        image.setImageData(imageBytes); // Set image binaries to image object
        image.setImageType(imageRepository.findImageTypeRecnumByTypeName(fileFormat.toUpperCase())); // Find the corresponding image type from IMAGE_TYPE table and set to image entity


        comparison.setImageWords(image.getText()); // Set extracted text from image to comparison object
    }

    // Save image entity to database
    public void saveImageToDatabase(){
        imageRepository.save(image);
    }





}
