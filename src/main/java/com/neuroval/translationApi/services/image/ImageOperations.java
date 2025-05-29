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
    private List<String> imageTextList = new ArrayList<>();
    private String extractedText = "";
    private static final Logger logger = Log.getLogger(ImageOperations.class);

    /**
     * Extract text from the image using tesseract
     *
     * @param multipartFile
     * @param languageCode
     * @return
     * @throws IOException
     * @throws TesseractException
     */
    public String extractTextFromImage(MultipartFile multipartFile, String languageCode) throws IOException, TesseractException {

        // Convert MultipartFile to File
        File tempFile = convertMultipartFileToFile(multipartFile);

        // Get the os name
        String osName = System.getProperty("os.name");
        logger.warn("Detected system OS is: {}", osName);

        // Initialize Tesseracts
        Tesseract tesseract = new Tesseract();

        // Assign tesseract datapath based on OS system
        if (osName.toLowerCase().contains("windows")) {

            // Set Tesseract data path windows
            tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");
            logger.warn("Tesseract datapath is: \"C:/Program Files/Tesseract-OCR/tessdata\"");

        } else if (osName.toLowerCase().contains("linux")) {

            // Set Tesseract data path ubuntu
            tesseract.setDatapath("/usr/share/tesseract-ocr/5/tessdata");
            logger.warn("Tesseract datapath is: /usr/share/tesseract-ocr/5/tessdata");

        }

        // Set language
        tesseract.setLanguage(languageCode);
        logger.warn("Tesseract language is: {}", languageCode);

        // Perform OCR
        extractedText = tesseract.doOCR(tempFile);
        logger.warn("OCR has been performed!\nImage has been processed!");

        // Delete temp file after processing
        tempFile.delete();
        logger.warn("Temp file has been deleted!");

        // Map the extracted text to Java object
        mapper(extractedText);

        //imageTextList = splitWords(extractedText); // Split the words
        // Split the words
        imageTextList.addAll(splitWords(extractedText));

        // Save image bytes
        imageBytes = multipartFile.getBytes();
        logger.info("Image has been processed!");

        logger.warn("The text successfully extracted from the image: {}", extractedText);

        return extractedText;
    }

    /**
     * Convert multiple part file to single file
     *
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
     *
     * @param text
     * @return
     */
    public String mapper(String text) {
        image = new Image();
        image.setText(text);
        logger.info("Extracted text deserialized to imageText:", image.getText());
        return image.getText();
    }

    /**
     * Split the words from entire string and map them to a List
     *
     * @param text
     * @return
     */
    public List<String> splitWords(String text) {

        // Split the string by spaces
        String[] wordsArray = text.split("\\s+");

        // Convert array to ArrayList
        List<String> wordsList = new ArrayList<>(Arrays.asList(wordsArray));

        logger.info("Words has been split!");

        // Set image word list
        image.setTextList(wordsList);

        return wordsList;
    }

    /**
     * Find the uploaded image file format and return it
     *
     * @param imageFile
     * @return
     */
    public String getFileFormat(MultipartFile imageFile) {

        // Get file format and set to fileFormat;
        fileFormat = imageFile.getOriginalFilename().substring(imageFile.getOriginalFilename().lastIndexOf("."));
        logger.warn("image file format is: {}", fileFormat);
        return fileFormat;
    }

    /**
     * Map image to image entity
     */
    public void mapToImageEntity() {
        // Create new object of image
        image.setTextList(imageTextList);

        // Set extracted text from image to image object
        image.setText(extractedText);

        // Set image binaries to image object
        image.setImageData(imageBytes);

        // Find the corresponding image type from IMAGE_TYPE table and set to image entity
        image.setImageType(imageRepository.findImageTypeRecnumByTypeName(fileFormat.toUpperCase()));

        // Set extracted text from image to comparison object
        comparison.setImageWords(extractedText);
        logger.info("uploaded image successfully mapped to IMAGE entity", image.toString());

        // reset the imagetextlist after every object mapping
        imageTextList = new ArrayList<>();
    }

    /**
     * Save image entity to database
     */
    public void saveImageToDatabase() {
        imageRepository.save(image);
        logger.warn("Extracted text from image successfully saved to database!");
    }
}
