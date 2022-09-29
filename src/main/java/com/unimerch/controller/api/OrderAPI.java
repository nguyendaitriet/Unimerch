package com.unimerch.controller.api;

import com.unimerch.dto.order.OrderCardResult;
import com.unimerch.dto.order.OrderChartResult;
import com.unimerch.dto.order.OrderData;
import com.unimerch.security.NameConstant;
import com.unimerch.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderAPI {
    @Autowired
    private OrderService orderService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<?> saveOrder(Authentication authentication, @RequestBody String data) {
        OrderData orderData = orderService.saveOrderData(data, authentication);
        return new ResponseEntity<>(orderData.getAsinList(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @GetMapping("/chart-all")
    public ResponseEntity<?> getChartsAll() {
        OrderChartResult orderChart = orderService.getChartAllAcc();
        return new ResponseEntity<>(orderChart, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('MANAGER','USER')")
    @GetMapping("/chart-acc/{id}")
    public ResponseEntity<?> getChartsUser(@PathVariable Integer id) {
        OrderChartResult orderChart = orderService.getChartUser(id);
        return new ResponseEntity<>(orderChart, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('MANAGER','USER')")
    @GetMapping("/chart-grp/{id}")
    public ResponseEntity<?> getChartsGroup(@PathVariable Integer id) {
        OrderChartResult orderChart = orderService.getChartGroup(id);
        return new ResponseEntity<>(orderChart, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @GetMapping("/card-all")
    public ResponseEntity<?> getSaleCardsAll() {
        Map<String, OrderCardResult> orderCards = orderService.getCardAllAcc();
        return new ResponseEntity<>(orderCards, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('MANAGER','USER')")
    @GetMapping("/card-acc/{id}")
    public ResponseEntity<?> getSaleCardsUser(@PathVariable Integer id) {
        Map<String, OrderCardResult> orderCards = orderService.getCardsUser(id);
        return new ResponseEntity<>(orderCards, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('MANAGER','USER')")
    @GetMapping("/card-grp/{id}")
    public ResponseEntity<?> getSaleCardsGroup(@PathVariable Integer id) {
        Map<String, OrderCardResult> orderCards = orderService.getCardsGroup(id);
        return new ResponseEntity<>(orderCards, HttpStatus.OK);
    }

}