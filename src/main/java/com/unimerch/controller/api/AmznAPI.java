package com.unimerch.controller.api;

import com.unimerch.dto.amznacc.AmznAccAnalyticsItemResult;
import com.unimerch.dto.amznacc.AmznAccFilterItemResult;
import com.unimerch.dto.amznacc.AmznAccParam;
import com.unimerch.dto.amznacc.AmznAccResult;
import com.unimerch.service.AmznUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/amzn")
public class AmznAPI {

    @Autowired
    private AmznUserService amznUserService;

    @PutMapping("/updateMetadata")
    public ResponseEntity<?> updateMetadata(Authentication authentication, @RequestBody String data) {
        amznUserService.updateMetadata(data, authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/updateStatus")
    public ResponseEntity<?> updateStatus(Authentication authentication, @RequestBody String status) {
        amznUserService.updateStatus(status, authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @PostMapping
    public DataTablesOutput<AmznAccResult> findAllAmznAccountsPageable(@Valid @RequestBody DataTablesInput input) {
        return amznUserService.findAll(input);
    }

    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @GetMapping("/findAllAmznAccs")
    public ResponseEntity<?> findAllAmznAccounts() {
        return new ResponseEntity<>(amznUserService.findAll(), HttpStatus.OK);
    }

    //    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @PostMapping("/create")
    public ResponseEntity<?> createAmznAcc(@Validated @RequestBody AmznAccParam amznAccCreateParam) {
        AmznAccResult newAmznAcc = amznUserService.create(amznAccCreateParam);
        return new ResponseEntity<>(newAmznAcc, HttpStatus.CREATED);
    }

    //    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @PostMapping("/import")
    public ResponseEntity<?> importNewAmznAcc(@RequestParam MultipartFile fileUploadAmznAcc) throws IOException {
        List<AmznAccResult> amznAccResultList = amznUserService.importFile(fileUploadAmznAcc);
        return new ResponseEntity<> (amznAccResultList, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAmznAcc(@RequestBody AmznAccParam amznAccParam, @PathVariable String id) {
        amznUserService.update(id, amznAccParam);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteAmznAcc(@PathVariable String id){
        amznUserService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @GetMapping("/findAllFilter")
    public ResponseEntity<?> findAllAmznAccFilter() {
        List<AmznAccFilterItemResult> amznAccResults = amznUserService.findAllFilter();
        return new ResponseEntity<>(amznAccResults, HttpStatus.OK);
    }

    @GetMapping("/analytics-all")
    public ResponseEntity<?> findAllAnalytics() {
        List<AmznAccAnalyticsItemResult> analyticsList = amznUserService.findAllAnalytics();
        return new ResponseEntity<>(analyticsList, HttpStatus.OK);
    }

    @GetMapping("/analytics-grp/{id}")
    public ResponseEntity<?> findAnalyticsByGroupId(@PathVariable String id) {
        List<AmznAccAnalyticsItemResult> analyticsList = amznUserService.findAnalyticsByGrpId(id);
        return new ResponseEntity<>(analyticsList, HttpStatus.OK);
    }

    @GetMapping("/analytics-acc/{id}")
    public ResponseEntity<?> findAnalyticsByAmznAccId(@PathVariable String id) {
        List<AmznAccAnalyticsItemResult> analyticsList = amznUserService.findAnalyticsByAmznAccId(id);
        return new ResponseEntity<>(analyticsList, HttpStatus.OK);
    }

    @GetMapping("/die-all")
    public ResponseEntity<?> findAllAccountDie() {
        return null;
    }

    @GetMapping("/die-grp/{id}")
    public ResponseEntity<?> findAccountDieByGrpId(@PathVariable String id) {
        return null;
    }
}
