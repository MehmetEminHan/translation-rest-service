package com.neuroval.translationApi.rest.xliffController;

import com.neuroval.translationApi.model.XLIFF.TransUnit;
import com.neuroval.translationApi.services.XliffMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import jakarta.xml.bind.JAXBException;


@RestController
@RequestMapping("neuroval/translatition/validation/xliff")
public class XliffController {

    // Upload xliff file and serialize it to java XLIFF object
    @PostMapping("/upload")
    public List<TransUnit> uploadXliff(@RequestParam("file") MultipartFile file) throws IOException, JAXBException {
        return XliffMapper.xliffMapper(file);
    }

    // Send post request to learn is xliff end point awake
    @PostMapping("/isAwake")
    public String isAwake() {
        return "xliff api is awake";
    }
}
