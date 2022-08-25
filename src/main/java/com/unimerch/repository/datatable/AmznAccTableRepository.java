package com.unimerch.repository.datatable;

import com.unimerch.repository.model.AmznAccount;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmznAccTableRepository extends DataTablesRepository<AmznAccount, Integer>  {

}
