package com.unimerch.dto.old_system;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OProductPriceParam {
    private String ASIN;
    private String PriceHTML;
}
