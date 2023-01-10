package com.unimerch.dto.old_system;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OProductPriceParam {
    @JsonProperty("ASIN")
    private String ASIN;
    @JsonProperty("PriceHTML")
    private String PriceHTML;
}
