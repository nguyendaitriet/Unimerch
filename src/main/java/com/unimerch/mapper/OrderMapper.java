package com.unimerch.mapper;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.unimerch.dto.order.OrderCardResult;
import com.unimerch.dto.order.OrderChartResult;
import com.unimerch.dto.order.OrderData;
import com.unimerch.repository.model.order.Order;
import com.unimerch.repository.model.product.Product;
import com.unimerch.util.TimeUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OrderMapper extends StdDeserializer<OrderData> {

    public OrderMapper() {
        this(null);
    }

    public OrderMapper(Class<?> vc) {
        super(vc);
    }

    @Override
    public OrderData deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JacksonException {

        JsonNode orderNode = jsonParser.getCodec().readTree(jsonParser);
        JsonNode orderNodeList = orderNode.fields().next().getValue();
        OrderData orderData = new OrderData();
        List<Order> orderList = new ArrayList<>();
        Set<String> asinList = new HashSet<>();

        for (JsonNode node : orderNodeList) {

            String asin = node.get("asin").textValue();
            Instant date = TimeUtils.parseStringToInstant(node.get("period").textValue());
            String title = node.get("asinName").textValue();
            String info = node.get("variationInfo").toString();
            Integer purchased = node.get("unitsSold").asInt();
            Integer cancelled = node.get("unitsCancelled").asInt();
            Integer returned = node.get("unitsReturned").asInt();
            BigDecimal revenue = BigDecimal.valueOf(node.get("revenue").get("value").asDouble());
            BigDecimal royalties = BigDecimal.valueOf(node.get("royalties").get("value").asDouble());
            String currency = node.get("revenue").get("code").textValue();

            asinList.add(asin);
            orderList.add(new Order(asin, date, title, info, purchased, cancelled, returned, revenue, royalties, currency));
        }

        List<Product> productList = asinList.stream().map(Product::new).collect(Collectors.toList());
        orderData.setProductList(productList);
        orderData.setOrderList(orderList);
        orderData.setAsinList(asinList);
        return orderData;
    }

    public OrderCardResult toOrderCardResult(List<Order> ordersList, String date) {
        OrderCardResult cardItem = new OrderCardResult();

        Integer numberSold = 0;
        Integer cancelled = 0;
        Integer returned = 0;
        BigDecimal royalties = BigDecimal.ZERO;

        for (Order order : ordersList) {
            numberSold += order.getPurchased();
            cancelled += order.getCancelled();
            returned += order.getReturned();
            royalties = royalties.add(order.getRoyalties());
        }

        Integer purchased = numberSold - cancelled;

        return cardItem
                .setDate(date)
                .setPurchased(purchased)
                .setCancelled(cancelled)
                .setSold(numberSold)
                .setReturned(returned)
                .setRoyalties(royalties);
    }

    public OrderChartResult toOrderChartResult(List<String> dates, List<BigDecimal> royalties, List<Integer> soldNumbers) {
        return new OrderChartResult()
                .setDates(dates)
                .setRoyalties(royalties)
                .setSoldNumbers(soldNumbers);
    }
}