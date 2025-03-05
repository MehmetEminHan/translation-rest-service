package com.neuroval.translationApi.rest.xliffController;

import com.neuroval.translationApi.model.XLIFF.TransUnit;
import com.neuroval.translationApi.model.XLIFF.Xliff;
import com.neuroval.translationApi.model.image.Image;
import com.neuroval.translationApi.services.ComparisonOperations;
import com.neuroval.translationApi.services.ImageOperations;
import com.neuroval.translationApi.services.XliffOperations;
import net.sourceforge.tess4j.TesseractException;
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


    // Upload xliff file and serialize it to java XLIFF object
    @PostMapping("/translation")
    public List<TransUnit> uploadXliff(@RequestParam("file") MultipartFile file) throws IOException, JAXBException {
        return xliffOperations.mapper(file, xliff);
    }

    // Upload the image that has text and serialize the text to Java String
    @PostMapping("/image")
    public Map<String, Object> uploadImage(@RequestParam("file") MultipartFile file, @RequestHeader("LanguageCode") String languageCode) throws IOException, TesseractException {

        Map<String, Object> response = new HashMap<>();

        if (xliff.getFile() == null){
            response.put("status", "error");
            response.put("text", "\"Please send first a xliff file to \\\"neuroval/translatition/validation/xliff/upload/translation\\\"\"");
        }else{
            response.put("status", "successful");
            response.put("extracted-text", imageOperations.extractTextFromImage(file, languageCode, image = new Image()));
        }
        return response;
    }

    // Send post request to learn is xliff end point awake
    @GetMapping("/compare")
    public Map<String, Object> compareImageTextAndXliffText() {
        Map<String, Object> response = new HashMap<>();

        if ( comparisonOperations.compareXliffAndImage(image, xliff).toString().isEmpty()){
            response.put("status", "pass");
            response.put("message", "all words are matched!");
        }else{
            response.put("status", "failed");
            response.put("un-matched-words", comparisonOperations.compareXliffAndImage(image, xliff).toString());
        }

        return response;
    }

    // Send post request to learn is xliff end point awake
    @PostMapping("/isAwake")
    public String isAwake() {
        return "xliff api is awake";
    }
}
