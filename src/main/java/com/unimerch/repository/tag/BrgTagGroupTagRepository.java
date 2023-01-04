package com.unimerch.repository.tag;

import com.unimerch.repository.model.tag.BrgTagGroupTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrgTagGroupTagRepository extends JpaRepository<BrgTagGroupTag, Integer> {

    @Modifying
    void deleteAllByTagIdIn(List<Integer> tagIdList);
}
