package com.unimerch.repository.specification;

import com.unimerch.repository.model.User;

public class UserSpecification {
    public static Specification<User> notAdminAndCurrentUser(String principalUsername) {
        return ((root, query, criteriaBuilder)
                -> criteriaBuilder.notEqual(root.get("username").as(String.class), principalUsername));
    }
}
