package com.neuroval.translationApi.services.xliff;

import com.neuroval.translationApi.model.xliff.TransUnit;
import com.neuroval.translationApi.model.xliff.Xliff;
import com.neuroval.translationApi.model.xliff.xliff_1_2.TransUnit_1_2;
import com.neuroval.translationApi.model.xliff.xliff_1_2.Xliff_1_2;
import com.neuroval.translationApi.model.xliff.xliff_2_0.TransUnit_2_0;
import com.neuroval.translationApi.model.xliff.xliff_2_0.Xliff_2_0;
import com.neuroval.translationApi.services.log.Log;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.Data;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;



@Service
@Data
public class XliffOperations {

    private static final Logger logger = Log.getLogger(XliffOperations.class);  // Logger initialized for this class only once

    // Map xliff file to java XLIFF object
    public  List<TransUnit> mapper(MultipartFile file, Xliff xliff) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            JAXBContext context = JAXBContext.newInstance(Xliff.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            // Parse XLIFF XML from uploaded file
            Xliff xliff2 = (Xliff) unmarshaller.unmarshal(inputStream);
            logger.info(xliff2.toString());

            // Serialize the mapped xliff2 object to xliff java object
            xliff.setFile(xliff2.getFile());

            // Return list of translation units
            return xliff.getFile().getBody().getTransUnitList();
        } catch (JAXBException e) {
            throw new RuntimeException("Failed to parse XLIFF file", e);
        }
    }


    // Map xliff file to java XLIFF object with namespace_1_2
    public  List<TransUnit_1_2> mapper_1_2(MultipartFile file, Xliff_1_2 xliff_1_2) {
        try (InputStream inputStream = file.getInputStream()) {
            // Create XMLStreamReader to read the file
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(inputStream);

            // Detect namespace
            System.out.println(isThereNamespace(file));

            // Handle both namespaces with the same class
            // Create JAXBContext based on namespace
            JAXBContext context = JAXBContext.newInstance(Xliff_1_2.class);

            // Unmarshal the XLIFF XML file into Java objects
            Xliff_1_2 xliff_1_2v2 = (Xliff_1_2) context.createUnmarshaller().unmarshal(reader);
            logger.info(xliff_1_2v2.toString());

            // Serialize the mapped xliff2 object to the xliff Java object
            xliff_1_2.setFile(xliff_1_2v2.getFile());

            // Return list of translation units
            return xliff_1_2.getFile().getBody().getTransUnitList();

        } catch (JAXBException e) {
            throw new RuntimeException("Failed to parse XLIFF file", e);
        } catch (Exception e) {
            throw new RuntimeException("Error unmarshalling XLIFF", e);
        }
    }

    // Map xliff file to java XLIFF object with namespace_2_0
    public  List<TransUnit_2_0> mapper_2_0(MultipartFile file, Xliff_2_0 xliff_2_0) {
        try (InputStream inputStream = file.getInputStream()) {
            // Create XMLStreamReader to read the file
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(inputStream);

            // Detect namespace
            System.out.println(isThereNamespace(file));

            // Handle both namespaces with the same class
            // Create JAXBContext based on namespace
            JAXBContext context = JAXBContext.newInstance(Xliff_2_0.class);

            // Unmarshal the XLIFF XML file into Java objects
            Xliff_2_0 xliff_2_0v2 = (Xliff_2_0) context.createUnmarshaller().unmarshal(reader);
            logger.info(xliff_2_0v2.toString());

            // Serialize the mapped xliff2 object to the xliff Java object
            xliff_2_0.setFile(xliff_2_0v2.getFile());

            // Return list of translation units
            return xliff_2_0.getFile().getBody().getTransUnitList();

        } catch (JAXBException e) {
            logger.error(e.getMessage(), e); // log error detail
            throw new RuntimeException("Failed to parse XLIFF file", e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e); // log error detail
            throw new RuntimeException("Error unmarshalling XLIFF", e);
        }
    }

    // Find the namespace in the uploaded xliff file and return as a string
    public String isThereNamespace(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            // Create XMLStreamReader to read the file
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(inputStream);

            // Detect namespace
            String namespace = "";
            if (reader.hasNext()) {
                reader.next();
                if (reader.getEventType() == XMLStreamReader.START_ELEMENT) {
                    namespace = reader.getNamespaceURI();
                }
            }

            logger.info("Xliff file namespace is: {}", namespace);

            // Handle if namespace is null
            if (namespace == null) {
                namespace = "";
            }

            return namespace;
    } catch (XMLStreamException e) {
            logger.error(e.getMessage(), e); // log error detail
            throw new RuntimeException(e);
        }
    }
}
