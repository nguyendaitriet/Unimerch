package com.unimerch.service.impl;

import com.unimerch.dto.tag.TagResult;
import com.unimerch.exception.InvalidIdException;
import com.unimerch.mapper.TagMapper;
import com.unimerch.repository.model.tag.*;
import com.unimerch.repository.tag.BrgTagGroupTagRepository;
import com.unimerch.repository.tag.TagRepository;
import com.unimerch.repository.tag.TagGroupRepository;
import com.unimerch.service.TagGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagGroupServiceImpl implements TagGroupService {

    @Autowired
    private TagGroupRepository tagGroupRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private BrgTagGroupTagRepository brgTagGroupTagRepo;
    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<TagGroup> findAll() {
        return tagGroupRepository.findAll();
    }

    @Override
    public TagGroup findById(int id) {
        Optional<TagGroup> tag = tagGroupRepository.findById(id);
        if (tag.isPresent()) {
            return tag.get();
        }
        throw new InvalidIdException(messageSource.getMessage("validation.idNotExist", null, Locale.getDefault()));
    }

    @Override
    public TagGroup createTagGroup(TagGroup newTagGroup) {
        return tagGroupRepository.save(newTagGroup);
    }

    @Override
    public TagGroup updateTagGroup(TagGroup tagGroup) {
        return tagGroupRepository.save(tagGroup);
    }

    @Override
    public void deleteTagGroup(int id) {
        tagGroupRepository.deleteTagGroupAssociateProduct(id);
        tagGroupRepository.deleteTagGroupAssociateTag(id);
        tagGroupRepository.deleteById(id);
    }

    @Override
    public List<TagResult> addTagToTagGroup(List<Integer> tagContentIdList, int tagId) {
        TagGroup tagGroup = tagGroupRepository.findById(tagId).get();
//        List<Tag> tagContentList = tagContentRepository.findAllTagContentByIds(tagContentIdList);
        List<BrgTagGroupTag> brgTagGroupTag = tagContentIdList.stream().map(item -> {
            Tag tag = tagRepository.findById(item).get();
            BrgTagGroupTagId brgTagGroupTagId = new BrgTagGroupTagId(tagId, item);
            return new BrgTagGroupTag(brgTagGroupTagId, tagGroup, tag);
        }).collect(Collectors.toList());

        brgTagGroupTag = brgTagGroupTagRepo.saveAll(brgTagGroupTag);
        return brgTagGroupTag.stream().map(item -> tagMapper.toTagResult(item)).collect(Collectors.toList());
    }

    @Override
    public List<TagResult> getTagInsideTagGroup(int tagId) {
        List<Tag> tagList = tagRepository.findAllTagInsideTagGroup(tagId);
        return tagList.stream().map(item -> tagMapper.toTagResult(item)).collect(Collectors.toList());
    }

    @Override
    public List<TagResult> getTagOutsideTagGroup(int tagId) {
        List<Tag> tagList = tagRepository.findAllTagOutsideTagGroup(tagId);
        return tagList.stream().map(item -> tagMapper.toTagResult(item)).collect(Collectors.toList());
    }

    @Override
    public void deleteTagFromTagGroup(int tagContentId, int tagId) {
        tagRepository.deleteTagFromTagGroup(tagContentId, tagId);
    }

    @Override
    public void deleteMultiTagFromTagGroup(List<Integer> tagContentIdList, int tagId) {
        tagRepository.deleteMultiTagFromTagGroup(tagContentIdList, tagId);
    }
}
