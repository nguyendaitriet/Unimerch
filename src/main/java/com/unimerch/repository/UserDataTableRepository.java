package com.unimerch.repository;

import com.unimerch.repository.model.User;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

import java.util.function.Function;

@Repository
public interface UserDataTableRepository extends DataTablesRepository<User, Integer> {
}
