package com.unimerch.repository.product;

import com.unimerch.dto.product.ProductResult;
import com.unimerch.repository.model.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    @Query("SELECT NEW com.unimerch.dto.product.ProductResult (" +
                "SUM(o.purchased - o.cancelled), " +
                "o.title, " +
                "SUM(o.royalties), " +
                "p.price, " +
                "o.amznUser.username, " +
                "o.asin " +
            ")" +
            "FROM Order AS o " +
            "LEFT JOIN Product AS p " +
            "ON p.id = o.asin " +
            "WHERE o.date >= :startDay " +
            "GROUP BY o.asin, o.title, p.price, o.amznUser.username, o.asin")
    List<ProductResult> getProductItemResultList(@Param("startDay")Instant startDay);

    @Query("SELECT NEW com.unimerch.dto.product.ProductResult (" +
                "SUM(o.purchased - o.cancelled), " +
                "o.title, " +
                "SUM(o.royalties), " +
                "p.price, " +
                "o.amznUser.username, " +
                "o.asin " +
                ")" +
            "FROM Order AS o " +
            "LEFT JOIN Product AS p " +
            "ON p.id = o.asin " +
            "WHERE o.date >= :startDay " +
            "AND o.amznUser.id IN (:amznAccIds) " +
            "GROUP BY o.asin, o.title, p.price, o.amznUser.username, o.asin")
    List<ProductResult> getProductItemResultList(@Param("startDay")Instant startDay, @Param("amznAccIds") List<Integer> amznAccIds);

    @Query("SELECT NEW com.unimerch.dto.product.ProductResult (" +
                "SUM(o.purchased - o.cancelled), " +
                "o.title, " +
                "SUM(o.royalties), " +
                "p.price, " +
                "o.amznUser.username, " +
                "o.asin " +
                ")" +
            "FROM Order AS o " +
            "LEFT JOIN Product AS p " +
            "ON p.id = o.asin " +
            "WHERE o.date >= :startDay AND o.date <= :endDay " +
            "GROUP BY o.asin, o.title, p.price, o.amznUser.username, o.asin")
    List<ProductResult> getAllProductAnalyticsList(@Param("startDay") Instant startDay, @Param("endDay") Instant endDay);

    @Query("SELECT NEW com.unimerch.dto.product.ProductResult (" +
                "SUM(o.purchased - o.cancelled), " +
                "o.title, " +
                "SUM(o.royalties), " +
                "p.price, " +
                "o.amznUser.username, " +
                "o.asin " +
                ")" +
            "FROM Order AS o " +
            "LEFT JOIN Product AS p " +
            "ON p.id = o.asin " +
            "WHERE o.date >= :startDay AND o.date <= :endDay " +
            "AND (LOWER(o.asin) LIKE CONCAT('%',:key,'%') OR LOWER(o.title) LIKE CONCAT('%',:key,'%'))" +
            "GROUP BY o.asin, o.title, p.price, o.amznUser.username, o.asin")
    List<ProductResult> searchAllProductAnalyticsListByAsinOrTitle(@Param("key") String key, @Param("startDay") Instant startDay, @Param("endDay") Instant endDay);

    @Query("SELECT NEW com.unimerch.dto.product.ProductResult (" +
                "SUM(o.purchased - o.cancelled), " +
                "o.title, " +
                "SUM(o.royalties), " +
                "p.price, " +
                "o.amznUser.username, " +
                "o.asin " +
                ")" +
            "FROM Order AS o " +
            "LEFT JOIN Product AS p " +
            "ON p.id = o.asin " +
            "WHERE o.date >= :startDay AND o.date <= :endDay " +
            "AND o.amznUser.id IN ( " +
                "SELECT b.amznUser.id " +
                "FROM BrgGroupAmznUser AS b " +
                "WHERE b.group.id = :groupId " +
            ")" +
            "GROUP BY o.asin, o.title, p.price, o.amznUser.username, o.asin")
    List<ProductResult> getGroupProductAnalyticsList(@Param("groupId") Integer groupId, @Param("startDay") Instant startDay, @Param("endDay") Instant endDay);

    @Query("SELECT NEW com.unimerch.dto.product.ProductResult (" +
                "SUM(o.purchased - o.cancelled), " +
                "o.title, " +
                "SUM(o.royalties), " +
                "p.price, " +
                "o.amznUser.username, " +
                "o.asin " +
                ")" +
            "FROM Order AS o " +
            "LEFT JOIN Product AS p " +
            "ON p.id = o.asin " +
            "WHERE o.date >= :startDay AND o.date <= :endDay " +
            "AND o.amznUser.id IN ( " +
                "SELECT b.amznUser.id " +
                "FROM BrgGroupAmznUser AS b " +
                "WHERE b.group.id = :groupId " +
            ")" +
            "AND (LOWER(o.asin) LIKE CONCAT('%',:key,'%') OR LOWER(o.title) LIKE CONCAT('%',:key,'%'))" +
            "GROUP BY o.asin, o.title, p.price, o.amznUser.username, o.asin")
    List<ProductResult> searchGroupProductAnalyticsList(@Param("key") String key, @Param("groupId") Integer groupId, @Param("startDay") Instant startDay, @Param("endDay") Instant endDay);

    @Query("SELECT NEW com.unimerch.dto.product.ProductResult (" +
                "SUM(o.purchased - o.cancelled), " +
                "o.title, " +
                "SUM(o.royalties), " +
                "p.price, " +
                "o.amznUser.username, " +
                "o.asin " +
                ")" +
            "FROM Order AS o " +
            "LEFT JOIN Product AS p " +
            "ON p.id = o.asin " +
            "WHERE o.date >= :startDay AND o.date <= :endDay " +
            "AND o.amznUser.id = :amznId " +
            "GROUP BY o.asin, o.title, p.price, o.amznUser.username, o.asin")
    List<ProductResult> getAmznProductAnalyticsList(@Param("amznId") Integer amznId, @Param("startDay") Instant startDay, @Param("endDay") Instant endDay);

    @Query("SELECT NEW com.unimerch.dto.product.ProductResult (" +
                "SUM(o.purchased - o.cancelled), " +
                "o.title, " +
                "SUM(o.royalties), " +
                "p.price, " +
                "o.amznUser.username, " +
                "o.asin " +
                ")" +
            "FROM Order AS o " +
            "LEFT JOIN Product AS p " +
            "ON p.id = o.asin " +
            "WHERE o.date >= :startDay AND o.date <= :endDay " +
            "AND o.amznUser.id = :amznId " +
            "AND (LOWER(o.asin) LIKE CONCAT('%',:key,'%') OR LOWER(o.title) LIKE CONCAT('%',:key,'%'))" +
            "GROUP BY o.asin, o.title, p.price, o.amznUser.username, o.asin")
    List<ProductResult> searchAmznProductAnalyticsList(@Param("key") String key, @Param("amznId") Integer amznId, @Param("startDay") Instant startDay, @Param("endDay") Instant endDay);


}
