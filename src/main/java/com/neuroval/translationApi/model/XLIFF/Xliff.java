package com.neuroval.translationApi.model.XLIFF;

import jakarta.xml.bind.annotation.*;
import lombok.Data;


@XmlRootElement(name = "xliff")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Xliff {

    @XmlElement(name = "file")
    private File file;

    @XmlAttribute
    private String version;


}
