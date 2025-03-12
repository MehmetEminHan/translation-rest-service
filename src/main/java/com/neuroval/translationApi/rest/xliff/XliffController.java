package com.neuroval.translationApi.rest.xliff;

import com.neuroval.translationApi.services.exception.CorruptedFileException;
import com.neuroval.translationApi.services.exception.InvalidFileTypeException;
import com.neuroval.translationApi.model.xliff.TransUnit;
import com.neuroval.translationApi.model.xliff.Xliff;
import com.neuroval.translationApi.model.image.Image;
import com.neuroval.translationApi.services.exception.MissingXliffException;
import com.neuroval.translationApi.services.xliff.ComparisonOperations;
import com.neuroval.translationApi.services.image.ImageOperations;
import com.neuroval.translationApi.services.xliff.XliffOperations;
import net.sourceforge.tess4j.TesseractException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.xml.bind.JAXBException;



@RestController
@RequestMapping("neuroval/translatition/validation/xliff/upload")
public class XliffController {

    @Autowired
    private XliffOperations xliffOperations;
    @Autowired
    private ImageOperations imageOperations;
    @Autowired
    private ComparisonOperations comparisonOperations;

    @Autowired
    private Xliff xliff;
    @Autowired
    private Image image;

    private String fileFormat;
    private List<TransUnit> deserializedXliff;
    private String extractedText;
    private static final Logger logger = LogManager.getLogger(XliffController.class);


    /**
     * Handles the upload of an XLIFF file and converts it into a Java XLIFF object.
     * @param file
     * @return List<TransUnit> representing the serialized XLIFF object or throw an exceptions below if processing fails.
     * @throws IOException
     * @throws JAXBException
     */
    @PostMapping("/translation")
    public List<TransUnit> uploadXliff(@RequestParam("file") MultipartFile file) throws IOException, JAXBException {
        fileFormat = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")); // Get file format and set to fileFormat

        deserializedXliff = xliffOperations.mapper(file, xliff); // de-serialize the xliff file to java object

        if (fileFormat.toLowerCase().equals(".xliff")){
            logger.info("Uploaded Xliff file mapped to java object");
            logger.info(deserializedXliff);

            return deserializedXliff ; // Return mapped xliff file
        }else{
            throw new InvalidFileTypeException(fileFormat); // Throw an Invalid File Type Exception if user try to upload file format different then .xliff
        }
    }

    /**
     * Handles the upload of an image file containing text and extracts the text, converting it into a Java String.
     * @param imageFile
     * @param languageCode
     * @return  Map<String, Object> representing the extracted text from image as String or throw an exceptions below if processing fails.
     * @throws IOException
     * @throws TesseractException
     */
    @PostMapping("/image")
    public Map<String, Object> uploadImage(@RequestParam("image") MultipartFile imageFile, @RequestHeader("LanguageCode") String languageCode) throws IOException, TesseractException {
        Map<String, Object> response = new HashMap<>(); // Create response map

        fileFormat = imageFile.getOriginalFilename().substring(imageFile.getOriginalFilename().lastIndexOf(".")); // Get file format and set to fileFormat

        if (xliff.getFile() == null){
            throw new MissingXliffException(); // Throw a Missing file exception if user didn't upload any .xliff file
        }else{
            if (fileFormat.toLowerCase().equals(".png")){
                try {
                    extractedText = imageOperations.extractTextFromImage(imageFile, languageCode, image = new Image());
                    response.put("status", "successful"); // Set response status as successful
                    response.put("extracted-text", extractedText); // Set extracted-text response with extracted text from uploaded image

                    logger.info("uploaded image successfully parsed!\nExtracted text: " + extractedText);
                }catch (Exception e){
                    throw new CorruptedFileException(e.getMessage());
                }
            }else{
                throw new InvalidFileTypeException(fileFormat); // Throw an Invalid File Type Exception if user try to upload file format different then .png
            }
        }
        return response;
    }

    /**
     * Handles the upload of an image file and an XLIFF file, extracts text from the image, and compares it with the XLIFF content to identify unmatched words.
     * @return Map<String, Object> if they match return successful or containing a <String> of unmatched words with an error message if processing fails.
     */
    @GetMapping("/compare")
    public Map<String, Object> compareImageTextAndXliffText() {
        Map<String, Object> response = new HashMap<>(); // Create response map

        if (comparisonOperations.compareXliffAndImage(image, xliff).toString().isEmpty()){ // Return successful response status if there is no unmatched words
            response.put("status", "pass");
            response.put("message", "all words are matched!");

            logger.info("Comparison result:" + response);
        }else{ // Return fail response status if there is an unmatched words
            response.put("status", "failed");
            response.put("un-matched-words", comparisonOperations.compareXliffAndImage(image, xliff).toString());

            logger.warn("Comparison result:" + response);
        }
        return response;
    }

    /**
     * Sends a POST request to check if the XLIFF endpoint is active and responsive.
     * @return Map<String, Object> containing successful message
     */
    @PostMapping("/isAwake")
    public  Map<String, Object> isAwake() {
        Map<String, Object> response = new HashMap<>();

        response.put("status", "successful");

        logger.info("isAwake " + response.toString());

        return response;
    }
}
