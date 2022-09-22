package com.unimerch.controller.api;

import com.unimerch.dto.product.ProductPriceParam;
import com.unimerch.dto.product.ProductResult;
import com.unimerch.service.ProductService;
import com.unimerch.service.impl.ConfigurationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.module.Configuration;
import java.util.List;

@Controller
@RequestMapping("/api/products")
public class ProductAPI {
    @Autowired
    private ProductService productService;

    @PutMapping("/updatePrice")
    public ResponseEntity<?> updateProductPrice(@RequestBody ProductPriceParam productPriceParam) {
        productService.updateProduct(productPriceParam);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/findAllTodaySoldAllAmznAcc")
    public ResponseEntity<?> findAllTodaySoldAllAmznAcc() {
        List<ProductResult> productResultList = productService.findAllTodaySoldProduct(null, 3);
        return new ResponseEntity<>(productResultList, HttpStatus.OK);
    }

    @GetMapping("/findAllThisMonthSoldAllAmznAcc")
    public ResponseEntity<?> findAllThisMonthSoldAllAmznAcc() {
        List<ProductResult> productResultList = productService.findAllThisMonthSoldProduct(null, 3);
        return new ResponseEntity<>(productResultList, HttpStatus.OK);
    }

    @GetMapping("/findAllLast30DaysSoldAllAmznAcc")
    public ResponseEntity<?> findAllLast30DaysSoldAllAmznAcc() {
        List<ProductResult> productResultList = productService.findAllLast30DaysSoldProduct(null, 3);
        return new ResponseEntity<>(productResultList, HttpStatus.OK);
    }

    @GetMapping("/findAllTodaySoldEveryAmznAcc/{amznAccId}")
    public ResponseEntity<?> findAllTodaySoldEveryAmznAcc(@PathVariable Integer amznAccId) {
        List<ProductResult> productResultList = productService.findAllTodaySoldProduct(amznAccId,1);
        return new ResponseEntity<>(productResultList, HttpStatus.OK);
    }

    @GetMapping("/findAllThisMonthSoldEveryAmznAcc/{amznAccId}")
    public ResponseEntity<?> findAllThisMonthSoldEveryAmznAcc(@PathVariable Integer amznAccId) {
        List<ProductResult> productResultList = productService.findAllThisMonthSoldProduct(amznAccId,1);
        return new ResponseEntity<>(productResultList, HttpStatus.OK);
    }

    @GetMapping("/findAllLast30DaysSoldEveryAmznAcc/{amznAccId}")
    public ResponseEntity<?> findAllLast30DaysSoldEveryAmznAcc(@PathVariable Integer amznAccId) {
        List<ProductResult> productResultList = productService.findAllLast30DaysSoldProduct(amznAccId, 1);
        return new ResponseEntity<>(productResultList, HttpStatus.OK);
    }

    @GetMapping("/findAllTodaySoldInGroup/{groupId}")
    public ResponseEntity<?> findAllTodaySoldInGroup(@PathVariable Integer groupId) {
        List<ProductResult> productResultList = productService.findAllTodaySoldProduct(groupId, 2);
        return new ResponseEntity<>(productResultList, HttpStatus.OK);
    }

    @GetMapping("/findAllThisMonthSoldInGroup/{groupId}")
    public ResponseEntity<?> findAllThisMonthSoldInGroup(@PathVariable Integer groupId) {
        List<ProductResult> productResultList = productService.findAllThisMonthSoldProduct(groupId, 2);
        return new ResponseEntity<>(productResultList, HttpStatus.OK);
    }

    @GetMapping("/findAllLast30DaysSoldInGroup/{groupId}")
    public ResponseEntity<?> findAllLast30DaysSoldInGroup(@PathVariable Integer groupId) {
        List<ProductResult> productResultList = productService.findAllLast30DaysSoldProduct(groupId, 2);
        return new ResponseEntity<>(productResultList, HttpStatus.OK);
    }



}
