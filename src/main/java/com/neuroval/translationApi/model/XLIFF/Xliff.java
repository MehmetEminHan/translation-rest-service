package com.neuroval.translationApi.model.XLIFF;

import jakarta.xml.bind.annotation.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.util.Map;


@XmlRootElement(name = "xliff")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@Component
public class Xliff {
    //Elements
    @XmlElement(name = "file")
    private File file;

    //Attributes
    @XmlAttribute
    private String version;

    @XmlAnyAttribute //Keep unwanted attributes with their values in Map
    private Map<QName, String> otherAttributes;


}
