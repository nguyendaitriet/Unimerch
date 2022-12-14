//package com.unimerch.schedules;
//
//import com.unimerch.repository.model.order.Order;
//import com.unimerch.repository.order.OrderRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.time.Duration;
//import java.time.Instant;
//import java.util.List;
//import java.util.concurrent.ThreadLocalRandom;
//import java.util.stream.Collectors;
//
//@Component
//public class SeedingTask {
//    @Autowired
//    private OrderRepository orderRepository;
//
//    @Scheduled(cron = "0 10 0 * * *", zone = "Asia/Ho_Chi_Minh")
//    public void generateOrdersDaily() {
//        int orderNumber = getRandomNumber(20, 100);
//        List<Order> orders = orderRepository.findRandomOrders(orderNumber);
//        Instant today = Instant.now();
//        orders = orders.stream()
//                .map(item -> item.setId(0).setDate(today))
//                .collect(Collectors.toList());
//        orderRepository.saveAll(orders);
//    }
//
//    public int getRandomNumber(int min, int max) {
//        return (int) ((Math.random() * (max - min)) + min);
//    }
//
//
//    public static Instant between(Instant startInclusive, Instant endExclusive) {
//        long startSeconds = startInclusive.getEpochSecond();
//        long endSeconds = endExclusive.getEpochSecond();
//        long random = ThreadLocalRandom.current().nextLong(startSeconds, endSeconds);
//
//        return Instant.ofEpochSecond(random);
//    }
//}
