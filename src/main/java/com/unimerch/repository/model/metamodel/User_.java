package com.unimerch.repository.model.metamodel;

import com.unimerch.repository.model.Role;
import com.unimerch.repository.model.User;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(User.class)
public abstract class User_ {
    public static volatile SingularAttribute<User, String> username;
    public static volatile SingularAttribute<User, Role> role;
    public static final String USERNAME = "username";
    public static final String ROLE = "role";
}
