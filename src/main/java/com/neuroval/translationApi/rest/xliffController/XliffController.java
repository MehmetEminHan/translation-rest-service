package com.neuroval.translationApi.rest.xliffController;

import com.neuroval.translationApi.model.XLIFF.TransUnit;
import com.neuroval.translationApi.services.ImageOperations;
import com.neuroval.translationApi.services.XliffMapper;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

import jakarta.xml.bind.JAXBException;


@RestController
@RequestMapping("neuroval/translatition/validation/xliff/upload")
public class XliffController {

    @Autowired
    private ImageOperations imageOperations;
    private XliffMapper xliffMapper;

    // Upload xliff file and serialize it to java XLIFF object
    @PostMapping("/translation")
    public List<TransUnit> uploadXliff(@RequestParam("file") MultipartFile file) throws IOException, JAXBException {
        return xliffMapper.mapper(file);
    }

    // Upload the image that has text and serialize the text to Java String
    @PostMapping("/image")
    public String uploadImage(@RequestParam("file") MultipartFile file, @RequestHeader("LanguageCode") String languageCode) throws IOException, TesseractException {
        return imageOperations.extractTextFromImage(file, languageCode);
    }


    // Send post request to learn is xliff end point awake
    @PostMapping("/isAwake")
    public String isAwake() {
        return "xliff api is awake";
    }
}
