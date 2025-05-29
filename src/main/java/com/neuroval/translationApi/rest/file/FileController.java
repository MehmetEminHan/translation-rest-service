package com.neuroval.translationApi.rest.file;

import com.neuroval.translationApi.model.response.Response;
import com.neuroval.translationApi.services.comparison.ComparisonOperations;
import com.neuroval.translationApi.services.exception.InvalidFileTypeException;
import com.neuroval.translationApi.services.exception.InvalidFileTypeHeaderException;
import com.neuroval.translationApi.services.json.JsonOperations;
import com.neuroval.translationApi.services.xliff.XliffOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("translation/validation/file")
public class FileController {

    @Autowired
    private XliffOperations xliffOperations;
    @Autowired
    private JsonOperations jsonOperations;
    @Autowired
    private
    ComparisonOperations comparisonOperations;

    private Response response;
    private static final Logger logger = LogManager.getLogger(FileController.class);

    @PostMapping("/upload")
    public Response uploadFile(@RequestParam("file") MultipartFile file, @RequestHeader("file-type") String fileType) throws IOException {

        comparisonOperations.setFileType(fileType);

        response = new Response();

        // if file-type header value is "xliff"
        if (fileType.equalsIgnoreCase("xliff")) {

            // if the uploaded file extension is .xliff
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
                logger.error("Xliff file format is not supported! {}", file.getOriginalFilename());
                throw new InvalidFileTypeException(xliffOperations.getFileFormat(file));
            }

            // if file-type header value is "json"
        } else if (fileType.equalsIgnoreCase("json")) {

            // if the uploaded file extension is ".json"
            if (jsonOperations.getFileFormat(file).toLowerCase().equals(".json")) {
                response.setMessage("Json file is serialized!");
                response.setStatus("Successful");
                response.setData(jsonOperations.loadJsonAsMap(file));

                // map the uploaded xliff file to XLIFF object
                jsonOperations.mapToFileEntity(file);

                // Save the XLIFF object to database
                jsonOperations.saveJsonToDatabase();
            } else {
                // Throw an Invalid File Type Exception if user try to upload file format different then .json
                logger.error("Json file format is not supported! {}", file.getOriginalFilename());
                throw new InvalidFileTypeException(jsonOperations.getFileFormat(file));
            }

            // if the file-type header value is not both xliff or json throw an exception invalid file type header
        } else {
            throw new InvalidFileTypeHeaderException(fileType);
        }

        return response;
    }
}
