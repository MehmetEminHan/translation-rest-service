package com.neuroval.translationApi.model.xliff;

import jakarta.xml.bind.annotation.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.util.Map;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@Component
public class File {
    //Elements
    @XmlElement(name = "body")
    private Body body;

    //Attributes
    @XmlAttribute(name = "original")
    private String original;

    @XmlAttribute(name = "source-language")
    private String sourceLanguage;

    @XmlAttribute(name = "target-language")
    private String targetLanguage;

    @XmlAttribute(name = "datatype")
    private String dataType;

    @XmlAnyAttribute //Keep unwanted attributes with their values in Map
    private Map<QName, String> otherAttributes;
}
