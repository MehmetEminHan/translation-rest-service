package com.neuroval.translationApi.rest.image;

import com.neuroval.translationApi.model.comparison.Comparison;
import com.neuroval.translationApi.model.image.Image;
import com.neuroval.translationApi.rest.xliff.XliffController;
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
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("neuroval/translation/validation/image")
public class ImageController {

    @Autowired
    private ImageOperations imageOperations;
    @Autowired
    private Comparison comparison;

    @Autowired
    private Image image;
    @Autowired
    private XliffController xliffController;

    private String fileFormat;
    private String extractedText;
    private static final Logger logger = LogManager.getLogger(ImageController.class);

    /**
     * Handles the upload of an image file containing text and extracts the text, converting it into a Java String.
     * @param imageFile
     * @param languageCode
     * @return  Map<String, Object> representing the extracted text from image as String or throw an exceptions below if processing fails.
     * @throws IOException
     * @throws TesseractException
     */
    @PostMapping("/upload")
    public Map<String, Object> uploadImage(@RequestParam("image") MultipartFile imageFile, @RequestHeader("LanguageCode") String languageCode) throws IOException {
        Map<String, Object> response = new HashMap<>(); // Create response map

        fileFormat = imageFile.getOriginalFilename().substring(imageFile.getOriginalFilename().lastIndexOf(".")); // Get file format and set to fileFormat

        if (xliffController.getXliff().getFile() == null && xliffController.getXliff_1_2() == null && xliffController.getXliff_2_0() == null){
            throw new MissingXliffException(); // Throw a Missing file exception if user didn't upload any .xliff file
        }else{
            if (fileFormat.toLowerCase().equals(".png")){
                try {
                    extractedText = imageOperations.extractTextFromImage(imageFile, languageCode);
                    response.put("status", "successful"); // Set response status as successful

                    comparison.setImageWords(image.getText()); // Set extracted text from image to comparison object
                    image.setText(extractedText);
                    response.put("extracted-text", image.getTextList()); // Set extracted-text response with extracted text from uploaded image

                    logger.info("uploaded image successfully parsed!\nExtracted text: {}", extractedText);
                }catch (Exception e){
                    throw new CorruptedFileException(e.getMessage());
                }
            }else{
                throw new InvalidFileTypeException(fileFormat); // Throw an Invalid File Type Exception if user try to upload file format different then .png
            }
        }
        return response;
    }
}
