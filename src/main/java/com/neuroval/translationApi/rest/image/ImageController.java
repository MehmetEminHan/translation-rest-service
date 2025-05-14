package com.neuroval.translationApi.rest.image;

import com.neuroval.translationApi.model.image.Image;
import com.neuroval.translationApi.model.image.TesseractLanguages;
import com.neuroval.translationApi.model.response.Response;
import com.neuroval.translationApi.model.xliff.Xliff;
import com.neuroval.translationApi.model.xliff.xliff_1_2.Xliff_1_2;
import com.neuroval.translationApi.model.xliff.xliff_2_0.Xliff_2_0;
import com.neuroval.translationApi.services.exception.CorruptedFileException;
import com.neuroval.translationApi.services.exception.InvalidFileTypeException;
import com.neuroval.translationApi.services.exception.MissingXliffException;
import com.neuroval.translationApi.services.image.ImageOperations;
import net.sourceforge.tess4j.TesseractException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("translation/validation/image")
public class ImageController {

    @Autowired
    private Image image;
    @Autowired
    private Xliff_1_2 xliff_1_2;
    @Autowired
    private Xliff_2_0 xliff_2_0;
    @Autowired
    private Xliff xliff;
    @Autowired
    private ImageOperations imageOperations;
    @Autowired
    private TesseractLanguages tesseract;


    private static final Logger logger = LogManager.getLogger(ImageController.class);
    private Response response;

    /**
     * Handles the upload of an image file containing text and extracts the text, converting it into a Java String.
     * @param imageFile
     * @param languageCode
     * @return  Map<String, Object> representing the extracted text from image as String or throw an exceptions below if processing fails.
     * @throws IOException
     * @throws TesseractException
     */
    @PostMapping("/upload")
    public Response uploadImage(@RequestParam("image") MultipartFile imageFile, @RequestHeader("LanguageCode") String languageCode) throws IOException {
        response = new Response(); // Initialize the new response object and map the json response to response object

        if (xliff == null && xliff_1_2 == null && xliff_2_0 == null){
            throw new MissingXliffException(); // Throw a Missing file exception if user didn't upload any .xliff file
        }else{
            if (imageOperations.getFileFormat(imageFile).equalsIgnoreCase(".png")){
                try {
                    imageOperations.extractTextFromImage(imageFile, languageCode); // Extract the text from the image using the Tesseract software
                    imageOperations.mapToImageEntity();  // map the extracted IMAGE text to IMAGE object
                    imageOperations.getImage().setLanguageCode(languageCode);
                    imageOperations.saveImageToDatabase(); // Save the IMAGE object to database

                    response.setStatus("successful");
                    response.setMessage("Image extracted successfully!");
                    response.setData(imageOperations.getImage()); // Map the IMAGE object to RESPONSE object and return as json
                }catch (Exception e){
                    throw new CorruptedFileException(e.getMessage());
                }
            }else{
                throw new InvalidFileTypeException(imageOperations.getFileFormat(imageFile)); // Throw an Invalid File Type Exception if user try to upload file format different then .png
            }
        }
        return response;
    }

    /**
     * Handles the upload of an image file containing text and extracts the text, converting it into a Java String.
     * @param imageFiles
     * @param languageCode
     * @return  Map<String, Object> representing the extracted text from image as String or throw an exceptions below if processing fails.
     * @throws IOException
     * @throws TesseractException
     */
    @PostMapping("/multi-upload")
    public Response uploadMultipleImage(@RequestParam("images") MultipartFile[] imageFiles, @RequestHeader("LanguageCode") String languageCode) throws IOException {
        response = new Response(); // Initialize the new response object and map the json response to response object

        if (imageFiles == null || imageFiles.length == 0) {
            response.setStatus("error");
            response.setMessage("No images selected!");
            response.setData(imageOperations.getImage());
            return response;
        }

        for (MultipartFile imageFile : imageFiles) {
            if (imageOperations.getFileFormat(imageFile).equalsIgnoreCase(".png")){
                try {
                    imageOperations.extractTextFromImage(imageFile, languageCode); // Extract the text from the image using the Tesseract software
                }catch (Exception e){
                    throw new CorruptedFileException(e.getMessage());
                }
            }else{
                throw new InvalidFileTypeException(imageOperations.getFileFormat(imageFile)); // Throw an Invalid File Type Exception if user try to upload file format different then .png
            }
        }

        imageOperations.mapToImageEntity();  // map the extracted IMAGE text to IMAGE object
        imageOperations.getImage().setLanguageCode(languageCode);
        imageOperations.saveImageToDatabase(); // Save the IMAGE object to database

        response.setStatus("successful");
        response.setMessage("Image extracted successfully!");
        response.setData(imageOperations.getImage()); // Map the IMAGE object to RESPONSE object and return as json


        if (xliff == null && xliff_1_2 == null && xliff_2_0 == null){
            throw new MissingXliffException(); // Throw a Missing file exception if user didn't upload any .xliff file
        }else{

        }
        return response;
    }

    /**
     * Returns the tesseract language code for translation/validation/image/upload endpoint's LanguageCode header value
     * @return
     */
    @GetMapping("/language_codes")
    public Response getLanguageCodes() {
        response = new Response(); // Initialize the new response object and map the json response to response object
        response.setStatus("successful");
        response.setMessage("Language Codes returned successfully!");
        response.setData(TesseractLanguages.getLanguageCodes());
        return response;
    }
}
