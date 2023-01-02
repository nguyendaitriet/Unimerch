package com.unimerch.controller.api;

import com.unimerch.security.RoleConstant;
import com.unimerch.service.OrderService;
import com.unimerch.service.impl.ConfigurationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/amazone")
public class OldAPI {
    @Autowired
    private OrderService orderService;


    @RoleConstant.AuthenticatedUser
    @GetMapping("/updateOrder")
    public ResponseEntity<?> updateOrder() {

    }

    @RoleConstant.AuthenticatedUser
    @PutMapping("/updateProduct")
    public ResponseEntity<?> updateProduct(@RequestBody String newAppsConfig) {

    }

    @RoleConstant.ManagerAuthorization
    @PutMapping("/updateAccountStatus")
    public ResponseEntity<?> updateAccountStatus(@RequestBody String newBackendConfig) {

    }
    @RoleConstant.ManagerAuthorization
    @PutMapping("/updateMetadata")
    public ResponseEntity<?> updateAccountStatus(@RequestBody String newBackendConfig) {

    }


}
