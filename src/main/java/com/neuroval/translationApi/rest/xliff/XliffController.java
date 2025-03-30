package com.neuroval.translationApi.rest.xliff;

import com.neuroval.translationApi.model.comparison.Comparison;
import com.neuroval.translationApi.model.response.Response;
import com.neuroval.translationApi.model.xliff.xliff_1_2.TransUnit_1_2;
import com.neuroval.translationApi.model.xliff.xliff_2_0.TransUnit_2_0;
import com.neuroval.translationApi.services.exception.InvalidFileTypeException;
import com.neuroval.translationApi.model.xliff.TransUnit;
import com.neuroval.translationApi.services.comparison.ComparisonOperations;
import com.neuroval.translationApi.services.image.ImageOperations;
import com.neuroval.translationApi.services.xliff.XliffOperations;
import jakarta.transaction.Transactional;
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



    private List<TransUnit> deserializedXliff;
    private List<TransUnit_1_2> deserializedXliff_1_2;
    private List<TransUnit_2_0> deserializedXliff_2_0;
    private String extractedText;
    private static final Logger logger = LogManager.getLogger(XliffController.class);
    private String xliffFileNamespace;
    private Response response = new Response();


    /**
     * Handles the upload of an XLIFF file and converts it into a Java XLIFF object.
     * @param file
     * @return List<TransUnitRepository> representing the serialized XLIFF object or throw an exceptions below if processing fails.
     * @throws IOException
     * @throws JAXBException
     */
    @PostMapping("/upload")
    @Transactional
    public Response uploadXliff(@RequestParam("file") MultipartFile file) throws IOException, JAXBException {

        if (xliffOperations.getFileFormat(file).toLowerCase().equals(".xliff")){
            response.setStatus("Successfull");
            response.setMessage("Successfully uploaded xliff");
            response.setData(xliffOperations.mapToFileEntity(file)); // first map the sent it xliff file and set it to the response object

            logger.info("Uploaded Xliff file mapped to java object");

            xliffOperations.saveXliffToDatabase();

            return response;
        }else{
            throw new InvalidFileTypeException(xliffOperations.getFileFormat(file)); // Throw an Invalid File Type Exception if user try to upload file format different then .xliff
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
