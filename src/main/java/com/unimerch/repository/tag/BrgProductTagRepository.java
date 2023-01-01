package com.unimerch.repository.tag;

import com.unimerch.repository.model.BrgProductTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrgProductTagRepository extends JpaRepository<BrgProductTag, Integer> {
}
