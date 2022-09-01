package com.unimerch.repository.datatable;

import com.unimerch.repository.model.Order;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

public interface ProductTableRepository extends DataTablesRepository<Order, Integer> {

}
