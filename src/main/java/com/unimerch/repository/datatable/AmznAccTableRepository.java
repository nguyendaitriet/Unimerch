package com.unimerch.repository.datatable;

import com.unimerch.repository.model.amzn_user.AmznUser;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmznAccTableRepository extends DataTablesRepository<AmznUser, Integer>  {

}
