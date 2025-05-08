package com.neuroval.translationApi.rest.typo;

import com.neuroval.translationApi.model.response.Response;
import com.neuroval.translationApi.model.translation.Translation;
import com.neuroval.translationApi.services.image.ImageOperations;
import com.neuroval.translationApi.services.typo.TypoOperations;
import com.neuroval.translationApi.services.xliff.XliffOperations;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Getter
@Setter
@RequestMapping("translation/validation")
public class TypoController {

    @Autowired
    private TypoOperations typoOperations;
    @Autowired
    private XliffOperations xliffOperations;
    @Autowired
    private ImageOperations imageOperations;

    private Response response;
    @Autowired
    private Translation translation;

    @GetMapping("/image/typo")
    public Response findTypoFromImage() throws IOException {
        response = new Response();

        typoOperations.typoFinderEnglish(imageOperations.getImage().getText());
        typoOperations.mapToTypoEntity();

        response.setMessage("Possible spelling mistake found.");
        response.setStatus("Success");
        response.setData(typoOperations.getTypo());
        return response;
    }

    /*@GetMapping("/xliff/typo")
    public Response findTypoFromFile() throws IOException {
        response = new Response();

        typoOperations = new TypoOperations();
        typoOperations.typoFinderEnglish(xliffOperations.getTranslation().getTargetLanguageText())
        response.setMessage("Possible spelling mistake found.");
        response.setStatus("Success");
        response.setData(typoOperations.typoFinderEnglish(String.join(",",xliffOperations.getTranslation().getTargetLanguageText())));
        return response;
    }*/

}
