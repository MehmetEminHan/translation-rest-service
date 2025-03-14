package com.neuroval.translationApi.model.xliff.xliff_2_0;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAnyAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.util.List;
import java.util.Map;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@Component
public class Body_2_0 {
    //Elements
    @XmlElement(name = "trans-unit", namespace = "urn:oasis:names:tc:xliff:document:2.0")
    private List<TransUnit_2_0> transUnitList;

    //Attributes
    @XmlAnyAttribute //Keep unwanted attributes with their values in Map
    private Map<QName, String> otherAttributes;
}
