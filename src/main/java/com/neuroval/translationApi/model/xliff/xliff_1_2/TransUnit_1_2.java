package com.neuroval.translationApi.model.xliff.xliff_1_2;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAnyAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.util.Map;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@Component
public class TransUnit_1_2 {

    //Elements
    @XmlElement(namespace = "urn:oasis:names:tc:xliff:document:1.2")
    private String source;

    @XmlElement(namespace = "urn:oasis:names:tc:xliff:document:1.2")
    private String target;

    //Attributes
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes;

    @Override
    public String toString() {
        return "TransUnit{" +
                "source='" + source + '\'' +
                ", target='" + target + '\'' +
                ", otherAttributes=" + otherAttributes +
                '}';
    }
}
