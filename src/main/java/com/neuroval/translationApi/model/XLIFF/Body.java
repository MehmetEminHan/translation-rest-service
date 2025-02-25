package com.neuroval.translationApi.model.XLIFF;

import jakarta.xml.bind.annotation.*;
import lombok.Data;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Body {
    @XmlElement(name = "trans-unit")
    private List<TransUnit> transUnitList;
}
