package com.neuroval.translationApi.rest.comparison;

import com.neuroval.translationApi.model.response.Response;
import com.neuroval.translationApi.services.comparison.ComparisonOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("neuroval/translation/validation/comparison")
public class ComparisonController {

    @Autowired
    private ComparisonOperations comparisonOperations;

    private Response response;
    private Object responseObject;

    /**
     * Handles the upload of an image file and an XLIFF file, extracts text from the image, and compares it with the XLIFF content to identify unmatched words.
     * @return Map<String, Object> if they match return successful or containing a <String> of unmatched words with an error message if processing fails.
     */
    @GetMapping("/compare")
    public Response compareImageTextAndXliffText() {
        response = new Response(); // Initialize the new response object and map the json response to response object

        responseObject = comparisonOperations.compareXliffAndImage(); // Compare FILE text with IMAGE text and set to responseObject
        comparisonOperations.mapToFileEntity(); // map the extracted FILE text and IMAGE text to COMPARISON object
        comparisonOperations.saveComparisonToDatabase(); // Save the COMPARISON object to database

        response.setStatus("OK");
        response.setMessage("Comparing images and XLIFF texts");
        response.setData(comparisonOperations.getComparison()); // Map the responseObject (comparison object) object to RESPONSE object and return as json

        return response;
    }
}
