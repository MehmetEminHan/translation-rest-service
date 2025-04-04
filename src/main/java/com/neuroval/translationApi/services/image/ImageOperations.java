package com.neuroval.translationApi.services.image;

import com.neuroval.translationApi.model.comparison.Comparison;
import com.neuroval.translationApi.model.image.Image;
import com.neuroval.translationApi.repository.ImageRepository;
import com.neuroval.translationApi.services.log.Log;
import lombok.Data;
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


@Data
@Service
public class ImageOperations {

    @Autowired
    private Image image;
    @Autowired
    private Comparison comparison;
    @Autowired
    ImageRepository imageRepository;

    private byte[] imageBytes;
    private String fileFormat;
    private List<String> imageTextList;
    private String extractedText = "";
    private static final Logger logger = Log.getLogger(ImageOperations.class);

    /**
     * Extract text from the image using tesseract
     * @param multipartFile
     * @param languageCode
     * @return
     * @throws IOException
     * @throws TesseractException
     */
    public String extractTextFromImage(MultipartFile multipartFile, String languageCode) throws IOException, TesseractException {

        File tempFile = convertMultipartFileToFile(multipartFile); // Convert MultipartFile to File

        String osName = System.getProperty("os.name"); // Get the os name
        logger.info("Detected OS Name is: {}", osName);

        Tesseract tesseract = new Tesseract();  // Initialize Tesseracts

        // Assign tesseract datapath based on OS system
        if(osName.toLowerCase().contains("windows")) {
            tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata"); // Set Tesseract data path windows
        }else if(osName.toLowerCase().contains("linux")) {
            tesseract.setDatapath("/usr/share/tesseract-ocr/5/tessdata"); // Set Tesseract data path ubuntu
        }

        tesseract.setLanguage(languageCode); // Set language
        logger.info("Tesseract language set to {}", languageCode);

        extractedText = tesseract.doOCR(tempFile); // Perform OCR
        logger.info("Extracted text from image is: {}", extractedText);

        tempFile.delete(); // Delete temp file after processing

        mapper(extractedText); // Map the extracted text to Java object

        imageTextList = splitWords(extractedText); // Split the words

        imageBytes = multipartFile.getBytes(); // Save image bytes

        logger.warn("The text successfully extracted from image is: {}", extractedText);

        return extractedText;
    }

    /**
     * Convert multiple part file to single file
     * @param file
     * @return
     * @throws IOException
     */
    private File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File tempFile = File.createTempFile("uploaded", ".png");

        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(file.getBytes());
        }

        return tempFile;
    }

    /**
     * Map the extracted text to Image JAVA object
     * @param text
     * @return
     */
    public String mapper(String text){
        image.setText(text);
        logger.info("Extracted text deserialized to imageText:", image.getText());

        return image.getText();
    }

    /**
     * Split the words from entire string and map them to a List
     * @param text
     * @return
     */
    public List<String> splitWords(String text){
        String[] wordsArray = text.split("\\s+");  // Split the string by spaces

        List<String> wordsList = new ArrayList<>(Arrays.asList(wordsArray));  // Convert array to ArrayList

        logger.info("Split words: {}", wordsList);

        image.setTextList(wordsList);  // Set image word list

        return wordsList;
    }

    /**
     * Find the uploaded image file format and return it
     * @param imageFile
     * @return
     */
    public String getFileFormat(MultipartFile imageFile){
        fileFormat = imageFile.getOriginalFilename().substring(imageFile.getOriginalFilename().lastIndexOf(".")); // Get file format and set to fileFormat;
        return fileFormat;
    }

    /**
     * Map image to image entity
     */
    public void mapToImageEntity(){
        image = new Image(); // Create new object of image
        image.setTextList(imageTextList);
        image.setText(extractedText); // Set extracted text from image to image object
        image.setImageData(imageBytes); // Set image binaries to image object
        image.setImageType(imageRepository.findImageTypeRecnumByTypeName(fileFormat.toUpperCase())); // Find the corresponding image type from IMAGE_TYPE table and set to image entity
        comparison.setImageWords(extractedText); // Set extracted text from image to comparison object
        logger.info("uploaded image successfully parsed!\nExtracted text: {}", image.getText());
    }

    /**
     * Save image entity to database
     */
    public void saveImageToDatabase(){
        imageRepository.save(image);
        logger.info("Extracted text from image successfully saved to database");
    }





}
