package org.groomi.groomidevbackend.boilerplate_code.dto;
//switch domain for your domain (what owns the feature)
//and switch feature for the feature name
//package org.groomi.groomidevbackend.{domain}.dto.{feature};

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Request {
    private String stringField;
    private Integer numberField;
}
