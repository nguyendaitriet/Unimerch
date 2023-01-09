package com.unimerch.repository.order;

import com.unimerch.dto.order.OrderCardResult;
import com.unimerch.repository.model.order.Order;
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

    @Query(value = "SELECT NEW com.unimerch.dto.order.OrderCardResult (" +
                "SUM(o.purchased - o.cancelled), " +
                "SUM(o.purchased), " +
                "SUM(o.cancelled), " +
                "SUM(o.returned), " +
                "SUM(o.royalties)" +
            ")" +
            "FROM Order AS o " +
            "WHERE o.date >= :startDate AND o.date <= :endDate"
    )
    OrderCardResult getOrderCartResultAll(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query(value = "SELECT NEW com.unimerch.dto.order.OrderCardResult (" +
                "SUM(o.purchased - o.cancelled), " +
                "SUM(o.purchased), " +
                "SUM(o.cancelled), " +
                "SUM(o.returned), " +
                "SUM(o.royalties)" +
            ")" +
            "FROM Order AS o " +
            "WHERE o.date >= :startDate AND o.date <= :endDate " +
            "AND o.amznUser.id IN (" +
                "(SELECT br.amznUser.id " +
                "FROM BrgGroupAmznUser br " +
                "WHERE br.group.id = :groupId)" +
            ")"
    )
    OrderCardResult getOrderCartResultGroup(@Param("groupId") Integer groupId,@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query(value = "SELECT NEW com.unimerch.dto.order.OrderCardResult (" +
                "SUM(o.purchased - o.cancelled), " +
                "SUM(o.purchased), " +
                "SUM(o.cancelled), " +
                "SUM(o.returned), " +
                "SUM(o.royalties)" +
            ")" +
            "FROM Order AS o " +
            "WHERE o.date >= :startDate AND o.date <= :endDate " +
            "AND o.amznUser.id = :amznId"
    )
    OrderCardResult getOrderCartResultAmzn(@Param("amznId") Integer amznId,@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

}
