package com.unimerch.controller.api;

import com.unimerch.dto.analytics.AnalyticsParam;
import com.unimerch.security.RoleConstant;
import com.unimerch.service.AnalyticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsSaleAPI {

    @Autowired
    private AnalyticService analyticService;

    @RoleConstant.ManagerUserAuthorization
    @PostMapping
    public ResponseEntity<?> getAnalyticsChart(@RequestBody AnalyticsParam analyticsParam) {
        System.out.println(analyticsParam);
        return new ResponseEntity<> (analyticService.getAnalyticsChart(analyticsParam), HttpStatus.OK);
    }
}
