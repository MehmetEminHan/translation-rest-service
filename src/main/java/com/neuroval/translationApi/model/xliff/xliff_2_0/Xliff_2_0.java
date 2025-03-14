package com.neuroval.translationApi.model.xliff.xliff_2_0;

import jakarta.xml.bind.annotation.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.util.Map;

@XmlRootElement(name = "xliff", namespace = "urn:oasis:names:tc:xliff:document:2.0")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@Component
public class Xliff_2_0 {
    //Elements
    @XmlElement(name = "file", namespace = "urn:oasis:names:tc:xliff:document:2.0")
    private File_2_0 file;

    //Attributes
    @XmlAttribute
    private String version;

    @XmlAnyAttribute //Keep unwanted attributes with their values in Map
    private Map<QName, String> otherAttributes;
}
