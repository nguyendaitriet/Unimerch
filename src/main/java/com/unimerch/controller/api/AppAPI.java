package com.unimerch.controller.api;

import com.unimerch.service.impl.ConfigurationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/apps")
@PreAuthorize("isAuthenticated()")
public class AppAPI {
    @Autowired
    private ConfigurationServiceImpl configurationService;

    @GetMapping("/getAppsConfig")
    public ResponseEntity<?> getAppsConfigString() {
        return new ResponseEntity<>(configurationService.getAppsConfigString(), HttpStatus.OK);
    }

    @PutMapping("/updateAppsConfig")
    public ResponseEntity<?> updateAppsConfig(@RequestBody String newAppsConfig) {
        return new ResponseEntity<>(configurationService.updateAppsConfig(newAppsConfig), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @PutMapping("/updateBackendConfig")
    public ResponseEntity<?> updateBackendConfig(@RequestBody String newBackendConfig) {
        return new ResponseEntity<>(configurationService.updateBackendConfig(newBackendConfig), HttpStatus.OK);
    }


}
