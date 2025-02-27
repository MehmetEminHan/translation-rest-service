package com.neuroval.translationApi.services;

import com.neuroval.translationApi.model.XLIFF.TransUnit;
import com.neuroval.translationApi.model.XLIFF.Xliff;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class XliffMapper {

    //Map xliff file to java XLIFF object
    public  List<TransUnit> mapper(MultipartFile file) throws JAXBException, IOException {
        try (InputStream inputStream = file.getInputStream()) {
            JAXBContext context = JAXBContext.newInstance(Xliff.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            // Parse XLIFF XML from uploaded file
            Xliff xliff = (Xliff) unmarshaller.unmarshal(inputStream);

            // Return list of translation units
            return xliff.getFile().getBody().getTransUnitList();
        } catch (JAXBException e) {
            throw new RuntimeException("Failed to parse XLIFF file", e);
        }
    }
}
