package com.neuroval.translationApi.model.XLIFF;

import lombok.Data;
import jakarta.xml.bind.annotation.*;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class TransUnit {
    @XmlElement
    private String source;

    @XmlElement
    private String target;
}


