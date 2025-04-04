package com.neuroval.translationApi.model.xliff.xliff_2_0;

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
public class TransUnit_2_0 {

    public final String  test = "test";
    //Elements
    @XmlElement(namespace = "urn:oasis:names:tc:xliff:document:2.0")
    private String source;

    @XmlElement(namespace = "urn:oasis:names:tc:xliff:document:2.0")
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
