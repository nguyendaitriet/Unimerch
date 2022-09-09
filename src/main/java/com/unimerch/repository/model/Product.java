package com.unimerch.repository.model;

import com.unimerch.dto.product.ProductItemResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "products")
@NamedNativeQuery(
        name = "get_product_item_result",
        query =
                "SELECT  " +
                    "SUM(o.purchased - o.cancelled) AS quantitySold, " +
                    "o.title AS productName, " +
                    "SUM(o.royalties) AS royalties, " +
                    "p.price, " +
                    "o.amzn_account_id AS amznAccUsername, " +
                    "o.ASIN AS asin " +
                "FROM orders AS o " +
                "INNER JOIN products AS p " +
                "ON p.ASIN = o.ASIN " +
                "WHERE o.amzn_account_id IN (:amznAccId) " +
                "AND o.date >= :startDay " +
                "GROUP BY o.ASIN ",
        resultSetMapping = "product_item_result"
)
@SqlResultSetMapping(
        name = "product_item_result",
        classes = @ConstructorResult(
                targetClass = ProductItemResult.class,
                columns = {
                        @ColumnResult(name = "quantitySold", type = Integer.class),
                        @ColumnResult(name = "productName", type = String.class),
                        @ColumnResult(name = "royalties", type = BigDecimal.class),
                        @ColumnResult(name = "price", type = BigDecimal.class),
                        @ColumnResult(name = "amznAccUsername", type = String.class),
                        @ColumnResult(name = "asin", type = String.class)
                }
        )
)
@Accessors(chain = true)
public class Product {
    @Id
    @Column(name = "ASIN", nullable = false, length = 150)
    private String id;

    @Column(name = "price", precision = 12, scale = 2)
    private BigDecimal price;

    @Lob
    @Column(name = "price_HTML")
    private String priceHtml;


}