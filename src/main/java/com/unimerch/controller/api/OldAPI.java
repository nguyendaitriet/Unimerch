package com.unimerch.controller.api;

import com.jayway.jsonpath.ReadContext;
import com.unimerch.dto.amznacc.AmznStatus;
import com.unimerch.dto.amznacc.Metadata;
import com.unimerch.dto.old_system.OOrder;
import com.unimerch.dto.old_system.OProductPriceParam;
import com.unimerch.dto.old_system.Result;
import com.unimerch.dto.order.OrderData;
import com.unimerch.dto.product.ProductPriceParam;
import com.unimerch.security.RoleConstant;
import com.unimerch.service.amzn.AmznUserService;
import com.unimerch.service.config.ConfigurationServiceImpl;
import com.unimerch.service.order.OrderService;
import com.unimerch.service.product.ProductService;
import com.unimerch.shared.JacksonParser;
import com.unimerch.shared.JsonPathParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OldAPI {
    @Autowired
    private OrderService orderService;
    @Autowired
    private AmznUserService amznUserService;
    @Autowired
    private ConfigurationServiceImpl configurationService;
    @Autowired
    private ProductService productService;

    @RoleConstant.AuthenticatedUser
    @PostMapping("/api/amazone/updateOrder")
    public ResponseEntity<?> updateOrder(Authentication authentication, @RequestBody HashMap<String, String> fields) {
        OrderData orderData = orderService.saveOrderData(fields.get("json"), authentication);
        List<OOrder> data = orderData
                .getAsinList()
                .stream()
                .map(OOrder::new)
                .collect(Collectors.toList());
        return new ResponseEntity<>(new Result<>(data), HttpStatus.OK);
    }

    @RoleConstant.AuthenticatedUser
    @PostMapping("/api/amazone/updateMetadata")
    public ResponseEntity<?> updateMetadata(Authentication authentication, @RequestBody HashMap<String, String> fields) {
        String json = fields.get("json");
        ReadContext readContext = JsonPathParser.INSTANCE.parse(json);
        Metadata metadata = new Metadata()
                .setDailyProductCount(readContext.read("$.dailyProduct.count"))
                .setDailyProductLimit(readContext.read("$.dailyProduct.count"))
                .setOverallDesignCount(readContext.read("$.overallDesign.count"))
                .setOverallDesignLimit(readContext.read("$.overallDesign.count"))
                .setOverallProductCount(readContext.read("$.overallProduct.count"))
                .setOverallProductLimit(readContext.read("$.overallProduct.count"))
                .setTier(readContext.read("$.tier"))
                .setTotalRemoved(readContext.read("$.totalRemoved"))
                .setTotalRejected(readContext.read("$.totalRejected"));
        amznUserService.updateMetadata(metadata, authentication);
        return null;
    }

    @RoleConstant.AuthenticatedUser
    @PostMapping("/api/amazone/updateAccountStatus")
    public ResponseEntity<?> updateAccountStatus(Authentication authentication, @RequestBody HashMap<String, String> fields) {
        String json = fields.get("json");
        ReadContext readContext = JsonPathParser.INSTANCE.parse(json);
        AmznStatus amznStatus = new AmznStatus(readContext.read("$.AccountStatus"));
        amznUserService.updateStatus(amznStatus, authentication);
        return new ResponseEntity<>(new Result<>(new HashMap<String, String>() {{
            put("Status", amznStatus.getStatus());
        }}), HttpStatus.OK);
    }

    @RoleConstant.AuthenticatedUser
    @PostMapping("/api/amazone/updateProduct")
    public ResponseEntity<?> updateProduct(Authentication authentication, @RequestBody HashMap<String, String> fields) {
        String json = fields.get("json");
        List<ProductPriceParam> params = JacksonParser.INSTANCE
                .toList(json, OProductPriceParam.class)
                .stream()
                .map(old -> new ProductPriceParam(old.getASIN(), old.getPriceHTML()))
                .collect(Collectors.toList());
        productService.updateProducts(params);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/Scripts/application/configuration.json")
    public ResponseEntity<?> getConfiguration() {
        return new ResponseEntity<>(configurationService.getAppsConfigString(), HttpStatus.OK);
    }
}
