package com.unimerch.schedules;

import com.unimerch.repository.OrderRepository;
import com.unimerch.repository.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SeedingTask {
    @Autowired
    private OrderRepository orderRepository;

    @Scheduled(cron = "0 10 0 * * *", zone = "Asia/Ho_Chi_Minh")
    public void generateOrdersDaily() {
        int orderNumber = getRandomNumber(50, 100);
        List<Order> orders = orderRepository.findRandomOrders(orderNumber);
        Instant today = Instant.now();
        orders = orders.stream()
                .map(item -> item.setId(0).setDate(today))
                .collect(Collectors.toList());
        orderRepository.saveAll(orders);
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

}
