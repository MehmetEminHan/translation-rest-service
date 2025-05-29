package com.neuroval.translationApi.rest.comparison;

import com.neuroval.translationApi.model.image.Image;
import com.neuroval.translationApi.model.response.Response;
import com.neuroval.translationApi.model.xliff.Body;
import com.neuroval.translationApi.services.comparison.ComparisonOperations;
import com.neuroval.translationApi.services.exception.MissingImageException;
import com.neuroval.translationApi.services.exception.MissingTranslationException;
import com.neuroval.translationApi.services.image.ImageOperations;
import com.neuroval.translationApi.services.json.JsonOperations;
import com.neuroval.translationApi.services.xliff.XliffOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("translation/validation/comparison")
public class ComparisonController {

    @Autowired
    private ComparisonOperations comparisonOperations;
    @Autowired
    private XliffOperations xliffOperations;
    @Autowired
    private ImageOperations imageOperations;

    private Response response;
    private Object responseObject;
    @Autowired
    private JsonOperations jsonOperations;

    /**
     * Handles the upload of an image file and an XLIFF file, extracts text from the image, and compares it with the XLIFF content to identify unmatched words.
     *
     * @return Map<String, Object> if they match return successful or containing a <String> of unmatched words with an error message if processing fails.
     */
    @GetMapping("/compare")
    public Response compareImageTextAndXliffText() {

        // Initialize the new response object and map the json response to response object
        response = new Response();

        // if image object is null throw missing image exception
        if (imageOperations.getImage().getText() == null) throw new MissingImageException();

        // if xliff object is null throw missing xliff object exception
        else if (xliffOperations.getBody() == null && jsonOperations.getJson() == null) throw new MissingTranslationException();

        // else perform a comparison and return response object
        else {

            // Compare FILE text with IMAGE text and set to responseObject
            responseObject = comparisonOperations.compareTranslationAndImage();

            // map the extracted FILE text and IMAGE text to COMPARISON object
            comparisonOperations.mapToFileEntity();

            // Save the COMPARISON object to database
            comparisonOperations.saveComparisonToDatabase();

            // Map the responseObject (comparison object) object to RESPONSE object and return as json
            response.setStatus("OK");
            response.setMessage("Comparing images and XLIFF texts");
            response.setData(comparisonOperations.getComparison());

            return response;
        }
    }
}
