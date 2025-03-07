package com.neuroval.translationApi.model.XLIFF;

import lombok.Data;
import jakarta.xml.bind.annotation.*;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.util.Map;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@Component
public class TransUnit {
    //Elements
    @XmlElement
    private String source;

    @XmlElement
    private String target;

    //Attributes
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes;
}


