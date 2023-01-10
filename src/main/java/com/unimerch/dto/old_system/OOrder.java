package com.unimerch.dto.old_system;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class OOrder {
    @JsonProperty("ASIN")
    public String ASIN;
}
