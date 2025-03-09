package com.neuroval.translationApi.services.xliff;

import com.neuroval.translationApi.model.xliff.TransUnit;
import com.neuroval.translationApi.model.xliff.Xliff;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


@Service
public class XliffOperations {
    //Map xliff file to java XLIFF object
    public  List<TransUnit> mapper(MultipartFile file, Xliff xliff) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            JAXBContext context = JAXBContext.newInstance(Xliff.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            // Parse XLIFF XML from uploaded file
            Xliff xliff2 = (Xliff) unmarshaller.unmarshal(inputStream);

            // Serialize the mapped xliff2 object to xliff java object
            xliff.setFile(xliff2.getFile());

            // Return list of translation units
            return xliff.getFile().getBody().getTransUnitList();
        } catch (JAXBException e) {
            throw new RuntimeException("Failed to parse XLIFF file", e);
        }
    }
}
