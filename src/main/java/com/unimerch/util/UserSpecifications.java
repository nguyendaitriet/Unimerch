package com.unimerch.util;

import com.unimerch.repository.model.User;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Component
public class UserSpecifications {

//    private Specification<User> userWithoutThisId(Integer id) {
//        return new Specification<User>() {
//            @Override
//            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//                return criteriaBuilder.not(root.get(User_.));
//            }
//        };
//    }
}
