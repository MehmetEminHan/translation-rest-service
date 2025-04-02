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
    private Response response = new Response();

    /**
     * Handles the upload of an image file and an XLIFF file, extracts text from the image, and compares it with the XLIFF content to identify unmatched words.
     * @return Map<String, Object> if they match return successful or containing a <String> of unmatched words with an error message if processing fails.
     */
    @GetMapping("/compare")
    public Response compareImageTextAndXliffText() {
        response.setStatus("OK");
        response.setMessage("Comparing images and XLIFF texts");
        response.setData(comparisonOperations.compareXliffAndImage());

        comparisonOperations.saveComparisonToDatabase();

        return response;
    }
}
