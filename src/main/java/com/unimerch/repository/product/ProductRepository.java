package com.unimerch.repository.product;

import com.unimerch.dto.product.ProductResult;
import com.unimerch.repository.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    @Query(name = "get_product_item_result", nativeQuery = true)
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
            "WHERE o.date >= :startDay " +
            "GROUP BY o.asin, o.title, p.price, o.amznUser.username, o.asin")
    List<ProductResult> getProductItemResultList(@Param("startDay")Instant startDay);
}
