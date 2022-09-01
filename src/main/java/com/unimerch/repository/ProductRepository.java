package com.unimerch.repository;

import com.querydsl.core.Tuple;
import com.unimerch.dto.product.ProductItemResult;
import com.unimerch.repository.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    @Query(
        value = "SELECT  " +
                    "SUM(o.purchased)-SUM(o.cancelled) AS quantitySold, " +
                    "o.title AS productName, " +
                    "SUM(o.royalties) AS royalties, " +
                    "p.price, " +
                    "o.amzn_account_id AS amznAccUsername, " +
                    "o.ASIN AS asin " +
                "FROM orders AS o " +
                "INNER JOIN products AS p " +
                "ON p.ASIN = o.ASIN " +
                "WHERE o.amzn_account_id = :amznAccId " +
                "AND o.date >= :today " +
                "GROUP BY o.ASIN",
        nativeQuery = true
    )
    List<Tuple> getProductItemResultList(@Param("today")Instant today, @Param("amznAccId") Integer amznAccId);

}
