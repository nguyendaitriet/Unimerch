package com.unimerch.service.tag;

import com.unimerch.dto.product.ProductTagTagGroupParam;
import com.unimerch.dto.tag.TagParam;
import com.unimerch.dto.tag.TagResult;
import com.unimerch.exception.InvalidIdException;
import com.unimerch.mapper.TagMapper;
import com.unimerch.repository.model.tag.Tag;
import com.unimerch.repository.tag.BrgTagGroupTagRepository;
import com.unimerch.repository.tag.TagRepository;
import com.unimerch.service.tag.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private BrgTagGroupTagRepository brgTagGroupTagRepo;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private MessageSource messageSource;

    @Override
    public List<TagResult> findAll() {
        return tagRepository.findAll().stream().map(item -> tagMapper.toTagResult(item)).collect(Collectors.toList());
    }

    @Override
    public TagResult findById(int id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() ->
                new InvalidIdException(messageSource.getMessage("validation.idNotExist", null, Locale.getDefault())));
        return tagMapper.toTagResult(tag);
    }

    @Override
    public TagResult createTag(TagParam tagParam) {
        Tag newTag = tagMapper.toTag(tagParam);
        return tagMapper.toTagResult(tagRepository.save(newTag));
    }

    @Override
    public TagResult updateTag(TagParam tagParam) {
        Tag tag = tagMapper.toTag(tagParam);
        return tagMapper.toTagResult(tagRepository.save(tag));
    }

    @Override
    public void updateProductTagsByAsin(String asin, List<ProductTagTagGroupParam> productTagTagGroupParamList) {

    }

    @Override
    @Transactional
    public void deleteMultiTag(List<Integer> tagContentIdList) {
        brgTagGroupTagRepo.deleteAllByTagIdIn(tagContentIdList);
        tagRepository.deleteAllByIdIn(tagContentIdList);
    }
}
