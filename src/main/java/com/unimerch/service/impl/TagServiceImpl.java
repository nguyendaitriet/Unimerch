package com.unimerch.service.impl;

import com.unimerch.dto.tag_content.TagContentResult;
import com.unimerch.exception.InvalidIdException;
import com.unimerch.mapper.TagContentMapper;
import com.unimerch.repository.model.tag.*;
import com.unimerch.repository.tag.BrgTagTagContentRepository;
import com.unimerch.repository.tag.TagContentRepository;
import com.unimerch.repository.tag.TagRepository;
import com.unimerch.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private TagContentRepository tagContentRepository;
    @Autowired
    private BrgTagTagContentRepository brgTagTagContentRepo;
    @Autowired
    private TagContentMapper tagContentMapper;

    @Override
    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    @Override
    public Tag findById(int id) {
        Optional<Tag> tag = tagRepository.findById(id);
        if (tag.isPresent()) {
            return tag.get();
        }
        throw new InvalidIdException(messageSource.getMessage("validation.idNotExist", null, Locale.getDefault()));
    }

    @Override
    public Tag createTag(Tag newTag) {
        return tagRepository.save(newTag);
    }

    @Override
    public Tag updateTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    public void deleteTag(int id) {
        tagRepository.deleteTagAssociateProduct(id);
        tagRepository.deleteTagAssociateTagContent(id);
        tagRepository.deleteById(id);
    }

    @Override
    public List<TagContentResult> addTagContentToTag(List<Integer> tagContentIdList, int tagId) {
        Tag tag = tagRepository.findById(tagId).get();
//        List<TagContent> tagContentList = tagContentRepository.findAllTagContentByIds(tagContentIdList);
        List<BrgTagTagContent> brgTagTagContent = tagContentIdList.stream().map(item -> {
            TagContent tagContent = tagContentRepository.findById(item).get();
            BrgTagTagContentId brgTagTagContentId = new BrgTagTagContentId(tagId, item);
            return new BrgTagTagContent(brgTagTagContentId, tag, tagContent);
        }).collect(Collectors.toList());

        brgTagTagContent = brgTagTagContentRepo.saveAll(brgTagTagContent);
        return brgTagTagContent.stream().map(item -> tagContentMapper.toTagContentResult(item)).collect(Collectors.toList());
    }

    @Override
    public List<TagContentResult> getTagContentInsideTag(int tagId) {
        List<TagContent> tagContentList = tagContentRepository.findAllTagContentInsideTag(tagId);
        return tagContentList.stream().map(item -> tagContentMapper.toTagContentResult(item)).collect(Collectors.toList());
    }

    @Override
    public List<TagContentResult> getTagContentOutsideTag(int tagId) {
        List<TagContent> tagContentList = tagContentRepository.findAllTagContentOutsideTag(tagId);
        return tagContentList.stream().map(item -> tagContentMapper.toTagContentResult(item)).collect(Collectors.toList());
    }

    @Override
    public void deleteTagContentFromTag(int tagContentId, int tagId) {
        tagContentRepository.deleteTagContentFromTag(tagContentId, tagId);
    }

    @Override
    public void deleteMultiTagContentFromTag(List<Integer> tagContentIdList, int tagId) {
        tagContentRepository.deleteMultiTagContentFromTag(tagContentIdList, tagId);
    }
}
