package com.unimerch.service.impl;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.unimerch.dto.order.OrderData;
import com.unimerch.exception.ServerErrorException;
import com.unimerch.mapper.OrderMapper;
import com.unimerch.repository.AmznAccountRepository;
import com.unimerch.repository.OrderRepository;
import com.unimerch.repository.model.AmznAccount;
import com.unimerch.repository.model.Order;
import com.unimerch.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AmznAccountRepository amznAccountRepository;

    @Autowired
    private MessageSource messageSource;

    public void saveOrderData(String data, String jwt) {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(OrderData.class, new OrderMapper());
        mapper.registerModule(module);

        AmznAccount amznAccount = amznAccountRepository.getByUsername("26");

        try {
            OrderData orderData = mapper.readValue(data, OrderData.class);

            orderData.getOrderList().forEach(order -> {
                order.setAmznAccount(amznAccount);
                orderRepository.save(order);
            });
        } catch (JsonProcessingException | ServerErrorException e) {
            throw new ServerErrorException(messageSource.getMessage("error.500", null, Locale.getDefault()));
        }

    }
}
