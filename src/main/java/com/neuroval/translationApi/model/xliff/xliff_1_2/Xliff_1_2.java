package com.neuroval.translationApi.model.xliff.xliff_1_2;

import jakarta.xml.bind.annotation.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.util.Map;


@XmlRootElement(name = "xliff", namespace = "urn:oasis:names:tc:xliff:document:1.2")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@Component
public class Xliff_1_2 {
    //Elements
    @XmlElement(name = "file", namespace = "urn:oasis:names:tc:xliff:document:1.2")
    private File_1_2 file;

    //Attributes
    @XmlAttribute
    private String version;

    @XmlAnyAttribute //Keep unwanted attributes with their values in Map
    private Map<QName, String> otherAttributes;


}
