package com.unimerch.repository.datatable;

import com.unimerch.repository.model.User;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDataTableRepository extends DataTablesRepository<User, Integer> {
}