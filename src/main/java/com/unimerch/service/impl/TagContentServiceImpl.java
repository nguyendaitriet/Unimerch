package com.unimerch.service.impl;

import com.unimerch.dto.tag_content.TagContentParam;
import com.unimerch.dto.tag_content.TagContentResult;
import com.unimerch.exception.InvalidIdException;
import com.unimerch.mapper.TagContentMapper;
import com.unimerch.repository.model.tag.TagContent;
import com.unimerch.repository.tag.BrgTagTagContentRepository;
import com.unimerch.repository.tag.TagContentRepository;
import com.unimerch.service.TagContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagContentServiceImpl implements TagContentService {
    @Autowired
    private TagContentRepository tagContentRepository;
    @Autowired
    private BrgTagTagContentRepository brgTagTagContentRepo;
    @Autowired
    private TagContentMapper tagContentMapper;
    @Autowired
    private MessageSource messageSource;

    @Override
    public List<TagContentResult> findAll() {
        return tagContentRepository.findAll().stream().map(item -> tagContentMapper.toTagContentResult(item)).collect(Collectors.toList());
    }

    @Override
    public TagContentResult findById(int id) {
        TagContent tagContent = tagContentRepository.findById(id)
                .orElseThrow(() ->
                new InvalidIdException(messageSource.getMessage("validation.idNotExist", null, Locale.getDefault())));
        return tagContentMapper.toTagContentResult(tagContent);
    }

    @Override
    public TagContentResult createTagContent(TagContentParam tagContentParam) {
        TagContent newTagContent = tagContentMapper.toTagContent(tagContentParam);
        return tagContentMapper.toTagContentResult(tagContentRepository.save(newTagContent));
    }

    @Override
    public TagContentResult updateTagContent(TagContentParam tagContentParam) {
        TagContent tagContent = tagContentMapper.toTagContent(tagContentParam);
        return tagContentMapper.toTagContentResult(tagContentRepository.save(tagContent));
    }

    @Override
    @Transactional
    public void deleteMultiTagContent(List<Integer> tagContentIdList) {
        brgTagTagContentRepo.deleteAllByTagContentIdIn(tagContentIdList);
        tagContentRepository.deleteAllByIdIn(tagContentIdList);
    }
}
