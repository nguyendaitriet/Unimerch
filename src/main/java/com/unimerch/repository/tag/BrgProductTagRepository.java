package com.unimerch.repository.tag;

import com.unimerch.repository.model.tag.BrgProductTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrgProductTagRepository extends JpaRepository<BrgProductTag, Integer> {

    List<BrgProductTag> findByProductId(String asin);
}