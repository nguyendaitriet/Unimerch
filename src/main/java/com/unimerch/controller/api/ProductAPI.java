package com.unimerch.controller.api;

import com.unimerch.dto.product.ProductItemResult;
import com.unimerch.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/api/products")
public class ProductAPI {
    @Autowired
    private ProductService productService;

    @GetMapping("/findAllTodaySoldTableItem/{amznAccId}")
    public ResponseEntity<?> findAllTodaySoldTableItem(@PathVariable Integer amznAccId) {
        List<ProductItemResult> productItemResultList = productService.findAllTodaySoldProduct(amznAccId);
        return new ResponseEntity<>(productItemResultList, HttpStatus.OK);
    }

    @GetMapping("/findAllThisMonthSoldTableItem/{amznAccId}")
    public ResponseEntity<?> findAllThisMonthSoldTableItem(@PathVariable Integer amznAccId) {
        List<ProductItemResult> productItemResultList = productService.findAllThisMonthSoldProduct(amznAccId);
        return new ResponseEntity<>(productItemResultList, HttpStatus.OK);
    }

    @GetMapping("/findAllLast30DaysSoldTableItem/{amznAccId}")
    public ResponseEntity<?> findAllLast30DaysSoldTableItem(@PathVariable Integer amznAccId) {
        List<ProductItemResult> productItemResultList = productService.findAllLast30DaysSoldProduct(amznAccId);
        return new ResponseEntity<>(productItemResultList, HttpStatus.OK);
    }

}
