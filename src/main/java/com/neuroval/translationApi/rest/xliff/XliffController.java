package com.neuroval.translationApi.rest.xliff;

import com.neuroval.translationApi.model.response.Response;
import com.neuroval.translationApi.model.xliff.TransUnit;
import com.neuroval.translationApi.model.xliff.xliff_1_2.TransUnit_1_2;
import com.neuroval.translationApi.model.xliff.xliff_2_0.TransUnit_2_0;
import com.neuroval.translationApi.services.exception.InvalidFileTypeException;
import com.neuroval.translationApi.services.xliff.XliffOperations;
import jakarta.transaction.Transactional;
import jakarta.xml.bind.JAXBException;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@Getter
@Setter
@RequestMapping("translation/validation/xliff")
public class XliffController {

    @Autowired
    private XliffOperations xliffOperations;

    private Response response;
    private String extractedText;
    private String xliffFileNamespace;
    private List<TransUnit> deserializedXliff;
    private List<TransUnit_1_2> deserializedXliff_1_2;
    private List<TransUnit_2_0> deserializedXliff_2_0;
    private static final Logger logger = LogManager.getLogger(XliffController.class);

    /**
     * Handles the upload of an XLIFF file and converts it into a Java XLIFF object.
     *
     * @param file
     * @return List<TransUnitRepository> representing the serialized XLIFF object or throw an exceptions below if processing fails.
     * @throws IOException
     * @throws JAXBException
     */
    @PostMapping("/upload")
    @Transactional
    public Response uploadXliff(@RequestParam("file") MultipartFile file) throws IOException, JAXBException {

        // Initialize the new response object and map the json response to response object
        response = new Response();

        if (xliffOperations.getFileFormat(file).toLowerCase().equals(".xliff")) {

            // map the uploaded xliff file to XLIFF object
            xliffOperations.mapToFileEntity(file);

            // Save the XLIFF object to database
            xliffOperations.saveXliffToDatabase();

            // Map the XLIFF object transunit list to RESPONSE object and return as json
            response.setStatus("Success");
            response.setMessage("Successfully uploaded xliff");
            response.setData(xliffOperations.getTranslation());

            return response;
        } else {

            // Throw an Invalid File Type Exception if user try to upload file format different then .xliff
            throw new InvalidFileTypeException(xliffOperations.getFileFormat(file));
        }
    }

    /**
     * Sends a POST request to check if the XLIFF endpoint is active and responsive.
     *
     * @return Map<String, Object> containing successful message
     */
    @PostMapping("/isAwake")
    public Response isAwake() {

        // Initialize the new response object and map the json response to response object
        response = new Response();

        // Map the XLIFF object transunit list to RESPONSE object and return as json
        response.setStatus("Success");
        response.setMessage("neuroval/translation/validation/xliff/upload endpoint is awake!");
        response.setData(null);

        logger.info("isAwake {}", response.toString());
        return response;
    }
}
