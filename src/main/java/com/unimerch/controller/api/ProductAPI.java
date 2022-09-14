package com.unimerch.controller.api;

import com.unimerch.dto.product.ProductItemResult;
import com.unimerch.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/products")
public class ProductAPI {
    @Autowired
    private ProductService productService;

    @PutMapping("/update")
    private ResponseEntity<?> updateProduct(@RequestBody String data) {
        productService.updateProduct(data);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/findAllTodaySoldAllAmznAcc")
    public ResponseEntity<?> findAllTodaySoldAllAmznAcc() {
        List<ProductItemResult> productItemResultList = productService.findAllTodaySoldProduct(null, 3);
        return new ResponseEntity<>(productItemResultList, HttpStatus.OK);
    }

    @GetMapping("/findAllThisMonthSoldAllAmznAcc")
    public ResponseEntity<?> findAllThisMonthSoldAllAmznAcc() {
        List<ProductItemResult> productItemResultList = productService.findAllThisMonthSoldProduct(null, 3);
        return new ResponseEntity<>(productItemResultList, HttpStatus.OK);
    }

    @GetMapping("/findAllLast30DaysSoldAllAmznAcc")
    public ResponseEntity<?> findAllLast30DaysSoldAllAmznAcc() {
        List<ProductItemResult> productItemResultList = productService.findAllLast30DaysSoldProduct(null, 3);
        return new ResponseEntity<>(productItemResultList, HttpStatus.OK);
    }

    @GetMapping("/findAllTodaySoldEveryAmznAcc/{amznAccId}")
    public ResponseEntity<?> findAllTodaySoldEveryAmznAcc(@PathVariable Integer amznAccId) {
        List<ProductItemResult> productItemResultList = productService.findAllTodaySoldProduct(amznAccId,1);
        return new ResponseEntity<>(productItemResultList, HttpStatus.OK);
    }

    @GetMapping("/findAllThisMonthSoldEveryAmznAcc/{amznAccId}")
    public ResponseEntity<?> findAllThisMonthSoldEveryAmznAcc(@PathVariable Integer amznAccId) {
        List<ProductItemResult> productItemResultList = productService.findAllThisMonthSoldProduct(amznAccId,1);
        return new ResponseEntity<>(productItemResultList, HttpStatus.OK);
    }

    @GetMapping("/findAllLast30DaysSoldEveryAmznAcc/{amznAccId}")
    public ResponseEntity<?> findAllLast30DaysSoldEveryAmznAcc(@PathVariable Integer amznAccId) {
        List<ProductItemResult> productItemResultList = productService.findAllLast30DaysSoldProduct(amznAccId, 1);
        return new ResponseEntity<>(productItemResultList, HttpStatus.OK);
    }

    @GetMapping("/findAllTodaySoldInGroup/{groupId}")
    public ResponseEntity<?> findAllTodaySoldInGroup(@PathVariable Integer groupId) {
        List<ProductItemResult> productItemResultList = productService.findAllTodaySoldProduct(groupId, 2);
        return new ResponseEntity<>(productItemResultList, HttpStatus.OK);
    }

    @GetMapping("/findAllThisMonthSoldInGroup/{groupId}")
    public ResponseEntity<?> findAllThisMonthSoldInGroup(@PathVariable Integer groupId) {
        List<ProductItemResult> productItemResultList = productService.findAllThisMonthSoldProduct(groupId, 2);
        return new ResponseEntity<>(productItemResultList, HttpStatus.OK);
    }

    @GetMapping("/findAllLast30DaysSoldInGroup/{groupId}")
    public ResponseEntity<?> findAllLast30DaysSoldInGroup(@PathVariable Integer groupId) {
        List<ProductItemResult> productItemResultList = productService.findAllLast30DaysSoldProduct(groupId, 2);
        return new ResponseEntity<>(productItemResultList, HttpStatus.OK);
    }



}
