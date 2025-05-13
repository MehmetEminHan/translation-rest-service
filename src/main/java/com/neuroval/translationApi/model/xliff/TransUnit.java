package com.neuroval.translationApi.model.xliff;

import lombok.Data;
import jakarta.xml.bind.annotation.*;
import javax.xml.namespace.QName;
import java.util.Map;

@Data
@XmlAccessorType(XmlAccessType.FIELD)

public class TransUnit {
    //Elements
    @XmlElement
    private String source;

    @XmlElement
    private String target;

    //Attributes
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes;

    public TransUnit(String source, String target) {
        this.source = source;
        this.target = target;
    }

    @Override
    public String toString() {
        return "TransUnit{" +
                "source='" + source + '\'' +
                ", target='" + target + '\'' +
                ", otherAttributes=" + otherAttributes +
                '}';
    }
}


