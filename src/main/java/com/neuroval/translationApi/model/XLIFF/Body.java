package com.neuroval.translationApi.model.XLIFF;

import jakarta.xml.bind.annotation.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.util.List;
import java.util.Map;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@Component
public class Body {

    //Elements
    @XmlElement(name = "trans-unit")
    private List<TransUnit> transUnitList;

    //Attributes
    @XmlAnyAttribute //Keep unwanted attributes with their values in Map
    private Map<QName, String> otherAttributes;
}
