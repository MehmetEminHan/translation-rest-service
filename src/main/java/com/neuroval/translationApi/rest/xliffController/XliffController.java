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
    public String uploadImage(@RequestParam("file") MultipartFile file, @RequestHeader("LanguageCode") String languageCode) throws IOException, TesseractException {
        if (xliff.getFile() == null){
            return "Please send first a xliff file to \"neuroval/translatition/validation/xliff/upload/translation\"";
        }else{
            return imageOperations.extractTextFromImage(file, languageCode, image = new Image());
        }
    }

    // Send post request to learn is xliff end point awake
    @GetMapping("/compare")
    public Map<String, Object> compareImageTextAndXliffText() {
        Map<String, Object> response = new HashMap<>();
        for(String text : comparisonOperations.compareXliffAndImage(image, xliff)){
            response.put("text", text);
        }
        return response;
    }

    // Send post request to learn is xliff end point awake
    @PostMapping("/isAwake")
    public String isAwake() {
        return "xliff api is awake";
    }
}
