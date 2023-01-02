package com.unimerch.repository.tag;

import com.unimerch.repository.model.tag.BrgTagTagContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrgTagTagContentRepository extends JpaRepository<BrgTagTagContent, Integer> {
}
