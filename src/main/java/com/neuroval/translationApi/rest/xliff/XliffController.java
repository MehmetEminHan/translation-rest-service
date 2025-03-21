package com.neuroval.translationApi.rest.xliff;

import com.neuroval.translationApi.model.comparison.Comparison;
import com.neuroval.translationApi.model.xliff.xliff_1_2.TransUnit_1_2;
import com.neuroval.translationApi.model.xliff.xliff_1_2.Xliff_1_2;
import com.neuroval.translationApi.model.xliff.xliff_2_0.TransUnit_2_0;
import com.neuroval.translationApi.model.xliff.xliff_2_0.Xliff_2_0;
import com.neuroval.translationApi.services.exception.InvalidFileTypeException;
import com.neuroval.translationApi.model.xliff.TransUnit;
import com.neuroval.translationApi.model.xliff.Xliff;
import com.neuroval.translationApi.model.image.Image;
import com.neuroval.translationApi.services.comparison.ComparisonOperations;
import com.neuroval.translationApi.services.image.ImageOperations;
import com.neuroval.translationApi.services.xliff.XliffOperations;
import lombok.Getter;
import lombok.Setter;
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
@Getter
@Setter
@RequestMapping("neuroval/translation/validation/xliff")
public class XliffController {

    @Autowired
    private XliffOperations xliffOperations;
    @Autowired
    private ImageOperations imageOperations;
    @Autowired
    private ComparisonOperations comparisonOperations;
    @Autowired
    private Comparison comparison;

    @Autowired
    private Xliff xliff;
    @Autowired
    private Xliff_1_2 xliff_1_2;
    @Autowired
    private Xliff_2_0 xliff_2_0;
    @Autowired
    private Image image;

    private String fileFormat;
    private List<TransUnit> deserializedXliff;
    private List<TransUnit_1_2> deserializedXliff_1_2;
    private List<TransUnit_2_0> deserializedXliff_2_0;
    private String extractedText;
    private static final Logger logger = LogManager.getLogger(XliffController.class);
    private String xliffFileNamespace;


    /**
     * Handles the upload of an XLIFF file and converts it into a Java XLIFF object.
     * @param file
     * @return List<TransUnit> representing the serialized XLIFF object or throw an exceptions below if processing fails.
     * @throws IOException
     * @throws JAXBException
     */
    @PostMapping("/upload")
    public List<?> uploadXliff(@RequestParam("file") MultipartFile file) throws IOException, JAXBException {

        fileFormat = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")); // Get file format and set to fileFormat
        xliffFileNamespace = xliffOperations.isThereNamespace(file);

        if (fileFormat.toLowerCase().equals(".xliff")){
            try{
                if (xliffFileNamespace.equals("urn:oasis:names:tc:xliff:document:1.2")){
                    deserializedXliff_1_2 = xliffOperations.mapper_1_2(file);
                    logger.info(deserializedXliff_1_2);
                    comparison.setXliffWords(deserializedXliff_1_2); // Set deserialized xliff target language to comparison object
                    xliff_2_0 = new Xliff_2_0(); // Empty the fields
                    xliff = new Xliff(); // Empty the fields
                    return deserializedXliff_1_2;
                } else if (xliffFileNamespace.equals("")) {
                    deserializedXliff = xliffOperations.mapper(file);
                    logger.info(deserializedXliff);
                    comparison.setXliffWords(deserializedXliff); // Set deserialized xliff target language to comparison object
                    xliff_2_0 = new Xliff_2_0(); // Empty the fields
                    xliff_1_2 = new Xliff_1_2(); // Empty the fields
                    return deserializedXliff;
                }else if(xliffFileNamespace.equals("urn:oasis:names:tc:xliff:document:2.0")){
                    deserializedXliff_2_0 = xliffOperations.mapper_2_0(file);
                    logger.info(deserializedXliff_2_0);
                    comparison.setXliffWords(deserializedXliff_2_0); // Set deserialized xliff target language to comparison object
                    xliff = new Xliff(); // Empty the fields
                    xliff_1_2 = new Xliff_1_2(); // Empty the fields
                    return deserializedXliff_2_0;
                }
            }catch (NullPointerException e){
                logger.error(e);
            }

            logger.info("Uploaded Xliff file mapped to java object");

            comparison.setXliffWords(deserializedXliff); // Set deserialized xliff target language to comparison object
            return deserializedXliff;
        }else{
            throw new InvalidFileTypeException(fileFormat); // Throw an Invalid File Type Exception if user try to upload file format different then .xliff
        }
        //deserializedXliff = xliffOperations.mapper(file, xliff); // de-serialize the xliff file to java object
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
