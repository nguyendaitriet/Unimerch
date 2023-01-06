package com.unimerch.controller.api;

import com.unimerch.security.RoleConstant;
import com.unimerch.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
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
        return null;

    }

    @RoleConstant.AuthenticatedUser
    @PutMapping("/updateProduct")
    public ResponseEntity<?> updateProduct(@RequestBody String newAppsConfig) {
        return null;

    }

    @RoleConstant.ManagerAuthorization
    @PutMapping("/updateAccountStatus")
    public ResponseEntity<?> updateAccountStatus(@RequestBody String newBackendConfig) {
        return null;

    }
    @RoleConstant.ManagerAuthorization
    @PutMapping("/updateMetadata")
    public ResponseEntity<?> updateMetadata(@RequestBody String newBackendConfig) {
        return null;
    }


}
