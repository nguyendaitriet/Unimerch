package com.unimerch.repository.datatable;

import com.unimerch.repository.model.Group;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupDataTableRepository extends DataTablesRepository<Group, Integer> {

}
