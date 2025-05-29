package com.neuroval.translationApi.services.xliff;

import com.neuroval.translationApi.model.comparison.Comparison;
import com.neuroval.translationApi.model.translation.Translation;
import com.neuroval.translationApi.model.xliff.Body;
import com.neuroval.translationApi.model.xliff.TransUnit;
import com.neuroval.translationApi.model.xliff.xliff_1_2.TransUnit_1_2;
import com.neuroval.translationApi.model.xliff.xliff_2_0.TransUnit_2_0;
import com.neuroval.translationApi.repository.TranslationRepository;
import com.neuroval.translationApi.services.log.Log;
import lombok.Data;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


@Data
@Service
public class XliffOperations {

    @Autowired
    private Body body;
    @Autowired
    private Comparison comparison;
    @Autowired
    TranslationRepository translationRepository;

    private TransUnit transUnit;

    private Translation translation;
    private List<TransUnit> deserializedXliff;
    private List<TransUnit_1_2> deserializedXliff_1_2;
    private List<TransUnit_2_0> deserializedXliff_2_0;
    private String xliffType = "";
    private String fileFormat;


    private static final Logger logger = Log.getLogger(XliffOperations.class);  // Logger initialized for this class only once


    /**
     * Find the uploaded image file format and return it
     *
     * @param xliffFile
     * @return
     */
    public String getFileFormat(MultipartFile xliffFile) {
        fileFormat = xliffFile.getOriginalFilename().substring(xliffFile.getOriginalFilename().lastIndexOf(".")); // Get file format and set to fileFormat;
        logger.info("The uploaded file format is: {}", fileFormat);
        return fileFormat;
    }

    /**
     * Parse the xliff file uploaded by the user and map it to the corresponding java entity
     *
     * @param file
     * @return
     */
    public Translation mapToFileEntity(MultipartFile file) {

        // Create sourceText and targetText lists in method to clear them for every method run
        List<String> sourceText = new ArrayList<>();
        List<String> targetText = new ArrayList<>();

        // Create a new instance of translation to clear translation object for every method run
        translation = new Translation();

        // Find file type and save to translation file type
        translation.setFileType(translationRepository.findFileTypeRecnumByTypeName(getFileFormat(file).toUpperCase()));

        try {

            // Parse xliff file as target and source languages
            deserializedXliff = parse(file);

            logger.info(deserializedXliff);

            // Set deserialized xliff target language to comparison object
            comparison.setFileWords(deserializedXliff);

            //xliff_2_0 = new Xliff_2_0(); // Empty the fields
            //xliff_1_2 = new Xliff_1_2(); // Empty the fields
            //xliffType = "xliff_0_0";

            // Set sourceText and targetText list based on parsed xliff file
            for (TransUnit transUnit : body.getTransUnitList()) {
                sourceText.add(transUnit.getSource());
                targetText.add(transUnit.getTarget());
            }

            // Set sourceText and targetText lists into translation object sourceLanguageText and targetLanguageText fields
            translation.setSourceLanguageText(sourceText);
            translation.setTargetLanguageText(targetText);

            logger.warn("Uploaded Xliff file mapped to java object!");

            // Return translation
            return translation;

        } catch (NullPointerException | IOException e) {
            logger.error(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return translation;
    }

    /**
     * Save XLIFF entity to database
     */
    public void saveXliffToDatabase() {
        translationRepository.save(translation); // save translation object to the database
        logger.info("{}Translation (Xliff) saved to database!", translation.toString());
    }

    public List<TransUnit> parse(MultipartFile xliffFile) throws Exception {

        // 1. Initialize an empty list to store translation units
        List<TransUnit> translations = new ArrayList<>();

        // 2. Open the uploaded XLIFF file as an input stream (auto-closed by try-with-resources)
        try (InputStream inputStream = xliffFile.getInputStream()) {

            // 3. Create a DocumentBuilder to parse the XML
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            // 4. Parse the XML content from the input stream into a DOM Document
            Document doc = dBuilder.parse(inputStream);
            doc.getDocumentElement().normalize();

            // 5. Get all <trans-unit> elements from the XML
            NodeList transUnitList = doc.getElementsByTagName("trans-unit");

            // 6. Loop through each <trans-unit> element
            for (int i = 0; i < transUnitList.getLength(); i++) {
                Node node = transUnitList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    // 8. Extract <source> and <target> text from the element
                    String source = getTagValue("source", element);
                    String target = getTagValue("target", element);

                    // 9. Create a new TransUnit object and add it to the list
                    translations.add(new TransUnit(source, target));
                }
            }
        }

        // 10. Set the parsed list of trans-units into the body object (assumed to be a class-level variable)
        body.setTransUnitList(translations);

        return translations;
    }

    private String getTagValue(String tag, Element element) {

        // 1. Get all child nodes with the specified tag name from the given element
        NodeList nodeList = element.getElementsByTagName(tag);

        // 2. Check if the node list is not empty (i.e., the tag exists in the element)
        if (nodeList != null && nodeList.getLength() > 0) {

            // 3. Get the child nodes of the first matching tag (the actual content inside the tag)
            NodeList subList = nodeList.item(0).getChildNodes();

            // 4. Check if the child nodes are not empty (i.e., the tag contains actual data)
            if (subList != null && subList.getLength() > 0) {

                // 5. Return the node value (the text inside the tag)
                return subList.item(0).getNodeValue();
            }
        }

        // 6. Return an empty string if the tag or its value is not found
        return "";
    }
}
