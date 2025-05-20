package com.neuroval.translationApi.rest.image;

import com.neuroval.translationApi.model.image.Image;
import com.neuroval.translationApi.model.image.TesseractLanguages;
import com.neuroval.translationApi.model.response.Response;
import com.neuroval.translationApi.model.xliff.Body;
import com.neuroval.translationApi.model.xliff.Xliff;
import com.neuroval.translationApi.model.xliff.xliff_1_2.Xliff_1_2;
import com.neuroval.translationApi.model.xliff.xliff_2_0.Xliff_2_0;
import com.neuroval.translationApi.services.exception.CorruptedFileException;
import com.neuroval.translationApi.services.exception.InvalidFileTypeException;
import com.neuroval.translationApi.services.exception.MissingMultiImageException;
import com.neuroval.translationApi.services.exception.MissingXliffException;
import com.neuroval.translationApi.services.image.ImageOperations;
import com.neuroval.translationApi.services.log.Log;
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
    private Body body;
    @Autowired
    private ImageOperations imageOperations;

    private Response response;
    private static final Logger logger = Log.getLogger(ImageOperations.class);

    /**
     * Handles the upload of an image file containing text and extracts the text, converting it into a Java String.
     *
     * @param imageFile
     * @param languageCode
     * @return Map<String, Object> representing the extracted text from image as String or throw an exceptions below if processing fails.
     * @throws IOException
     * @throws TesseractException
     */
    @PostMapping("/upload")
    public Response uploadImage(@RequestParam("image") MultipartFile imageFile, @RequestHeader("LanguageCode") String languageCode) throws IOException {

        // Initialize the new response object and map the json response to response object
        response = new Response();

        // Check if user didn't upload any .xliff file
        if (body.getTransUnitList() == null) {

            logger.error("Missing xliff file");
            // Throw a Missing file exception if user didn't upload any .xliff file
            throw new MissingXliffException();

        } else {

            // Get uploaded image format if it is equals to png perform image parsing operation
            // If not user cannot perform a parsing operations because tesseract only works with png files
            if (imageOperations.getFileFormat(imageFile).equalsIgnoreCase(".png")) {

                try {

                    // Extract the text from the image using the Tesseract software
                    imageOperations.extractTextFromImage(imageFile, languageCode);

                    // map the extracted IMAGE text to IMAGE object
                    imageOperations.mapToImageEntity();
                    imageOperations.getImage().setLanguageCode(languageCode);

                    // Save the IMAGE object to database
                    imageOperations.saveImageToDatabase();

                    // Map the IMAGE object to RESPONSE object and return as json
                    response.setStatus("successful");
                    response.setMessage("Image extracted successfully!");
                    response.setData(imageOperations.getImage());

                } catch (Exception e) {
                    logger.error("Image extract failed", e.getMessage());
                    // If uploaded image is png but tesseract cannot use it throw a corrupted custom exception
                    throw new CorruptedFileException(e.getMessage());
                }

            } else {
                logger.error("Invalid image type: ", imageOperations.getFileFormat(imageFile));
                // Throw an Invalid File Type Exception if user try to upload file format different then .png
                throw new InvalidFileTypeException(imageOperations.getFileFormat(imageFile));
            }
        }

        return response;
    }

    /**
     * Handles the upload of an image file containing text and extracts the text, converting it into a Java String.
     *
     * @param imageFiles
     * @param languageCode
     * @return Map<String, Object> representing the extracted text from image as String or throw an exceptions below if processing fails.
     * @throws IOException
     * @throws TesseractException
     */
    @PostMapping("/multi-upload")
    public Response uploadMultipleImage(@RequestParam("images") MultipartFile[] imageFiles, @RequestHeader("LanguageCode") String languageCode) throws IOException {

        // Initialize the new response object and map the json response to response object
        response = new Response();

        // If user didn't upload any image return no images selected error message
        if (imageFiles == null || imageFiles.length == 0 || imageFiles[0].getOriginalFilename().equals("")) {
            throw new MissingMultiImageException();
        }

        // Check if user didn't upload any .xliff file
        if (body.getTransUnitList() == null) {

            // Throw a Missing file exception if user didn't upload any .xliff file
            throw new MissingXliffException();

        } else {

            for (MultipartFile imageFile : imageFiles) {
                if (imageOperations.getFileFormat(imageFile).equalsIgnoreCase(".png")) {
                    try {

                        // Extract the text from the image using the Tesseract software
                        imageOperations.extractTextFromImage(imageFile, languageCode);

                    } catch (Exception e) {

                        // Throw an Invalid File Type Exception if user try to upload file format different then .png
                        throw new CorruptedFileException(e.getMessage());

                    }
                } else {
                    throw new InvalidFileTypeException(imageOperations.getFileFormat(imageFile));
                }
            }

            // map the extracted IMAGE text to IMAGE object
            imageOperations.mapToImageEntity();
            imageOperations.getImage().setLanguageCode(languageCode);

            // Save the IMAGE object to database
            imageOperations.saveImageToDatabase();

            // Map the IMAGE object to RESPONSE object and return as json
            response.setStatus("successful");
            response.setMessage("Image extracted successfully!");
            response.setData(imageOperations.getImage());

            return response;
        }
    }

    /**
     * Returns the tesseract language code for translation/validation/image/upload endpoint's LanguageCode header value
     *
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
