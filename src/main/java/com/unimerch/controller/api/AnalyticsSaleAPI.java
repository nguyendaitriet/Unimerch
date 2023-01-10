package com.unimerch.controller.api;

import com.unimerch.dto.analytics.AnalyticsParam;
import com.unimerch.security.RoleConstant;
import com.unimerch.service.analytics.AnalyticService;
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
    @PostMapping("/getChartAnalytics")
    public ResponseEntity<?> getChartAnalytics(@RequestBody AnalyticsParam analyticsParam) {
        return new ResponseEntity<> (analyticService.getChartAnalytics(analyticsParam), HttpStatus.OK);
    }

    @RoleConstant.ManagerUserAuthorization
    @PostMapping("/getProductAnalytics")
    public ResponseEntity<?> getProductAnalytics(@RequestBody AnalyticsParam analyticsParam) {
        return new ResponseEntity<> (analyticService.getProductAnalyticsList(analyticsParam), HttpStatus.OK);
    }

    @RoleConstant.ManagerUserAuthorization
    @PostMapping("/getCardAnalytics")
    public ResponseEntity<?> getCardAnalytics(@RequestBody AnalyticsParam analyticsParam) {
        return new ResponseEntity<> (analyticService.getCardAnalytics(analyticsParam), HttpStatus.OK);
    }

}
