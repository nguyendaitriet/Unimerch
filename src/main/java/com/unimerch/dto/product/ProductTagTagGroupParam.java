package com.unimerch.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductTagTagGroupParam {
    private String asin;
    private Integer tagGroupId;
    private Integer tagId;
}
