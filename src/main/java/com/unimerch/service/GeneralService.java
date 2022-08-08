package com.unimerch.service;

import java.util.List;
import java.util.Optional;

public interface GeneralService<T> {

    List<T> findAll();

    Optional<T> findById(Long id);

    T getById(Long id);

    T create(T t);

    T update(T t);

    void remove(Long id);
}
