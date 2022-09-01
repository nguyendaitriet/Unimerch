package com.unimerch.controller.api;

import com.unimerch.dto.order.OrderCardItemResult;
import com.unimerch.dto.order.OrderData;
import com.unimerch.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderAPI {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<?> saveOrder(@RequestBody String data) {
        OrderData orderData = orderService.saveOrderData(data, "");
        return new ResponseEntity<>(orderData.getAsinList(), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getSaleCardsAll() {
        Map<String, OrderCardItemResult> orderCards = orderService.getCardAllAcc();
        return new ResponseEntity<>(orderCards, HttpStatus.OK);
    }

    @GetMapping("/acc/{id}")
    public ResponseEntity<?> getSaleCardsUser(@PathVariable Integer id) {
        Map<String, OrderCardItemResult> orderCards = orderService.getCardsUser(id);
        return new ResponseEntity<>(orderCards, HttpStatus.OK);
    }

    @GetMapping("/grp/{id}")
    public ResponseEntity<?> getSaleCardsGroup(@PathVariable Integer id) {
        Map<String, OrderCardItemResult> orderCards = orderService.getCardsGroup(id);
        return new ResponseEntity<>(orderCards, HttpStatus.OK);
    }
}