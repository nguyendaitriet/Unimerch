package com.unimerch.mapper;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.unimerch.dto.order.OrderCardItemResult;
import com.unimerch.dto.order.OrderData;
import com.unimerch.repository.model.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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
        JsonNode ordertList = orderNode.fields().next().getValue();
        OrderData orderData = new OrderData();
        List<Order> orderList = new ArrayList<>();

        for (JsonNode node : ordertList) {

            String asin = node.get("asin").textValue();
            Instant date = Instant.parse(node.get("period").textValue());
            String title = node.get("asinName").textValue();
            String info  = node.get("variationInfo").toString();
            Integer purchased = node.get("unitsSold").asInt();
            Integer cancelled = node.get("unitsCancelled").asInt();
            Integer returned = node.get("unitsReturned").asInt();
            BigDecimal revenue = BigDecimal.valueOf(node.get("revenue").get("value").asDouble());
            BigDecimal royalties = BigDecimal.valueOf(node.get("royalties").get("value").asDouble());
            String currency = node.get("revenue").get("code").textValue();

            orderList.add(new Order(asin, date, title, info, purchased, cancelled, returned, revenue, royalties, currency));
        }

        orderData.setOrderList(orderList);
        return orderData;
    }

    public OrderCardItemResult toOrderCardItem(List<Order> ordersList, String date) {
        OrderCardItemResult today = new OrderCardItemResult();

        Integer purchased = 0;
        Integer cancelled = 0;
        Integer returned = 0;
        BigDecimal royalties = BigDecimal.ZERO;

        for (Order order : ordersList) {
            purchased += order.getPurchased();
            cancelled += order.getCancelled();
            returned += order.getReturned();
            royalties = royalties.add(order.getRoyalties());
        }

        Integer numberSold = purchased - cancelled;

        return today
                .setDate(date)
                .setPurchased(purchased)
                .setCancelled(cancelled)
                .setNumberSold(numberSold)
                .setReturned(returned)
                .setRoyalties(royalties);
    }
}