package com.unimerch.service.tag;

import com.unimerch.dto.product.ProductTagTagGroupParam;
import com.unimerch.dto.tag.TagParam;
import com.unimerch.dto.tag.TagResult;

import java.util.List;

public interface TagService {
    List<TagResult> findAll();

    TagResult findById(int id);

    TagResult createTag(TagParam tagParam);

    TagResult updateTag(TagParam tagParam);

    void updateProductTagsByAsin(String asin, List<ProductTagTagGroupParam> productTagTagGroupParamList);

    void deleteMultiTag(List<Integer> tagIdList);
}
