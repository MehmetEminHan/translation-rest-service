package com.neuroval.translationApi.model.XLIFF;

import jakarta.xml.bind.annotation.*;
import lombok.Data;
import org.springframework.stereotype.Component;


@XmlRootElement(name = "xliff")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@Component
public class Xliff {

    @XmlElement(name = "file")
    private File file;

    @XmlAttribute
    private String version;


}
