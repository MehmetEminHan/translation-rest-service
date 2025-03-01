package com.neuroval.translationApi.model.XLIFF;

import jakarta.xml.bind.annotation.*;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@Component
public class File {
    @XmlElement(name = "body")
    private Body body;

    @XmlAttribute(name = "original")
    private String original;

    @XmlAttribute(name = "source-language")
    private String sourceLanguage;

    @XmlAttribute(name = "target-language")
    private String targetLanguage;

    @XmlAttribute(name = "datatype")
    private String dataType;
}
