package com.neuroval.translationApi.model.XLIFF;

import lombok.Data;
import jakarta.xml.bind.annotation.*;
import org.springframework.stereotype.Component;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@Component
public class TransUnit {
    @XmlElement
    private String source;

    @XmlElement
    private String target;
}


