package com.unimerch.repository;

import com.unimerch.repository.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    void deleteByAmznUserId(Integer amznAccount_id);

    @Query("SELECT o " +
            "FROM Order o " +
            "WHERE o.date >= :timeStart")
    List<Order> findAllWithStartDate(@Param("timeStart") Instant timeStart);

    @Query("SELECT o " +
            "FROM Order o " +
            "WHERE o.date >= :timeStart " +
            "AND o.date < :timeEnd")
    List<Order> findAllWithTimeRange(@Param("timeStart") Instant timeStart,
                                     @Param("timeEnd") Instant timeEnd);

    List<Order> findByAmznUserId(Integer amznAccId);

    @Query("SELECT o " +
            "FROM Order o " +
            "WHERE o.date >= :timeStart " +
            "AND o.amznUser.id = :amznAccId ")
    List<Order> findByAmznAccIdWithStartDate(@Param("amznAccId") Integer amznAccId,
                                             @Param("timeStart") Instant timeStart);

    @Query("SELECT o " +
            "FROM Order o " +
            "WHERE o.date >= :timeStart " +
            "AND o.date < :timeEnd " +
            "AND o.amznUser.id = :amznAccId ")
    List<Order> findByAmznAccIdWithTimeRange(@Param("amznAccId") Integer amznAccId,
                                             @Param("timeStart") Instant timeStart,
                                             @Param("timeEnd") Instant timeEnd);

    @Query("SELECT o " +
            "FROM Order o " +
            "WHERE o.amznUser.id IN " +
            "(SELECT br.amznUser.id " +
            "FROM BrgGroupAmznUser br " +
            "WHERE br.group.id = :groupId)")
    List<Order> findByGroupId(@Param("groupId") Integer groupId);

    @Query("SELECT o " +
            "FROM Order o " +
            "WHERE o.date >= :timeStart " +
            "AND o.amznUser.id IN " +
            "(SELECT br.amznUser.id " +
            "FROM BrgGroupAmznUser br " +
            "WHERE br.group.id = :groupId)")
    List<Order> findByGroupIdWithStartDate(@Param("groupId") Integer groupId,
                                           @Param("timeStart") Instant timeStart);

    @Query("SELECT o " +
            "FROM Order o " +
            "WHERE o.date >= :timeStart " +
            "AND o.date < :timeEnd " +
            "AND o.amznUser.id IN " +
            "(SELECT br.amznUser.id " +
            "FROM BrgGroupAmznUser br " +
            "WHERE br.group.id = :groupId)")
    List<Order> findByGroupIdWithTimeRange(@Param("groupId") Integer groupId,
                                           @Param("timeStart") Instant timeStart,
                                           @Param("timeEnd") Instant timeEnd);

    void deleteAllByAmznUserIdIn(List<Integer> ids);

    @Query(value = "SELECT * " +
            "FROM orders " +
            "ORDER BY RAND() " +
            "LIMIT :number ",
            nativeQuery = true
    )
    List<Order> findRandomOrders(@Param("number") int number);
}
