package com.unimerch.repository;

import com.unimerch.repository.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    void deleteByAmznAccount_Id(Integer amznAccount_id);
}
