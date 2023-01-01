package com.unimerch.repository.model;

import com.unimerch.dto.order.OrderChartColumn;
import com.unimerch.dto.product.ProductResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(
        name = "orders",
        indexes = @Index(name = "idx_date", columnList = "date")
)
@NamedNativeQuery(
        name = "get_order_chart_column_result_all_acc",
        query =
                "SELECT  " +
                    "DATE_FORMAT(o.`date`, '%d/%m/%Y') AS date, " +
                    "SUM(o.royalties) AS royalties, " +
                    "SUM(o.purchased - o.cancelled) AS sold " +
                "FROM orders o " +
                "WHERE o.date BETWEEN :startDay AND :endDay " +
                "GROUP BY DATE_FORMAT(o.`date`, '%d/%m/%Y') ",
        resultSetMapping = "order_chart_column_result_all_acc"
)
@SqlResultSetMapping(
        name = "order_chart_column_result_all_acc",
        classes = @ConstructorResult(
                targetClass = OrderChartColumn.class,
                columns = {
                        @ColumnResult(name = "date", type = String.class),
                        @ColumnResult(name = "royalties", type = BigDecimal.class),
                        @ColumnResult(name = "sold", type = Integer.class),
                }
        )
)
@NamedNativeQuery(
        name = "get_order_chart_column_result_group",
        query =
                "SELECT  " +
                    "DATE_FORMAT(o.`date`, '%d/%m/%Y') AS date, " +
                    "SUM(o.royalties) AS royalties, " +
                    "SUM(o.purchased - o.cancelled) AS sold " +
                "FROM orders o " +
                "WHERE o.date BETWEEN :startDay AND :endDay " +
                "AND o.amzn_user_id IN ( " +
                        "SELECT b.amzn_user_id " +
                        "FROM brg_group_amzn_user AS b " +
                        "WHERE b.group_id = :groupId " +
                    ") " +
                "GROUP BY DATE_FORMAT(o.`date`, '%d/%m/%Y') ",
        resultSetMapping = "order_chart_column_result_group"
)
@SqlResultSetMapping(
        name = "order_chart_column_result_group",
        classes = @ConstructorResult(
                targetClass = OrderChartColumn.class,
                columns = {
                        @ColumnResult(name = "date", type = String.class),
                        @ColumnResult(name = "royalties", type = BigDecimal.class),
                        @ColumnResult(name = "sold", type = Integer.class),
                }
        )
)
@NamedNativeQuery(
        name = "get_order_chart_column_result_amzn",
        query =
                "SELECT  " +
                        "DATE_FORMAT(o.`date`, '%d/%m/%Y') AS date, " +
                        "SUM(o.royalties) AS royalties, " +
                        "SUM(o.purchased - o.cancelled) AS sold " +
                        "FROM orders o " +
                        "WHERE o.date BETWEEN :startDay AND :endDay " +
                        "AND o.amzn_user_id = :amznId " +
                        "GROUP BY DATE_FORMAT(o.`date`, '%d/%m/%Y') ",
        resultSetMapping = "order_chart_column_result_amzn"
)
@SqlResultSetMapping(
        name = "order_chart_column_result_amzn",
        classes = @ConstructorResult(
                targetClass = OrderChartColumn.class,
                columns = {
                        @ColumnResult(name = "date", type = String.class),
                        @ColumnResult(name = "royalties", type = BigDecimal.class),
                        @ColumnResult(name = "sold", type = Integer.class),
                }
        )
)
@Accessors(chain = true)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "ASIN", nullable = false, length = 150)
    private String asin;

    @Column(name = "date", nullable = false, updatable = false)
    private Instant date;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Lob
    @Column(name = "info")
    private String info;

    @Column(name = "purchased", nullable = false)
    private Integer purchased;

    @Column(name = "cancelled", nullable = false)
    private Integer cancelled;

    @Column(name = "returned", nullable = false)
    private Integer returned;

    @Column(name = "revenue", nullable = false, precision = 12, scale = 2)
    private BigDecimal revenue;

    @Column(name = "royalties", nullable = false, precision = 12, scale = 2)
    private BigDecimal royalties;

    @Column(name = "currency", nullable = false, length = 50)
    private String currency;

    @ManyToOne(optional = false)
    @JoinColumn(name = "amzn_user_id", nullable = false)
    private AmznUser amznUser;

    public Order(String asin, Instant date, String title, String info, Integer purchased, Integer cancelled, Integer returned, BigDecimal revenue, BigDecimal royalties, String currency) {
        this.asin = asin;
        this.date = date;
        this.title = title;
        this.info = info;
        this.purchased = purchased;
        this.cancelled = cancelled;
        this.returned = returned;
        this.revenue = revenue;
        this.royalties = royalties;
        this.currency = currency;
    }

    public Order(BigDecimal royalties,Integer purchased) {
        this.purchased = purchased;
        this.royalties = royalties;
    }
}