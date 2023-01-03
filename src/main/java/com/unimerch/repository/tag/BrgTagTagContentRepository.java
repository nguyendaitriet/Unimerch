package com.unimerch.repository.tag;

import com.unimerch.repository.model.tag.BrgTagTagContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrgTagTagContentRepository extends JpaRepository<BrgTagTagContent, Integer> {

    @Modifying
    void deleteAllByTagContentIdIn(List<Integer> tagContentIdList);
}
