package com.unimerch.controller.api;

import com.unimerch.dto.amznacc.AmznAccParam;
import com.unimerch.dto.amznacc.AmznAccResult;
import com.unimerch.dto.user.LoginParam;
import com.unimerch.repository.model.JwtResponse;
import com.unimerch.repository.model.User;
import com.unimerch.security.BeanNameConstant;
import com.unimerch.service.AmznAccountService;
import com.unimerch.service.impl.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.io.IOException;

@RestController
@RequestMapping("/api/amznAccounts")
public class AmznAccountAPI {

    @Autowired
    private AmznAccountService amznAccountService;
    @Autowired
    @Qualifier(BeanNameConstant.AMZN_AUTHENTICATION_MANAGER_NAME)
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @GetMapping("/loginAmznAcc")
    public ModelAndView showLoginFormAmznAcc() {
        return new ModelAndView("/login/loginAmznAcc");
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginParam user) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        AmznAccResult currentUser = amznAccountService.findByUsername(user.getUsername());
//        if (currentUser.isDisabled()) {
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        }

        String jwt = jwtService.generateTokenLogin(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        JwtResponse jwtResponse = new JwtResponse(
                jwt,
                currentUser.getId(),
                userDetails.getUsername(),
                currentUser.getUsername(),
                userDetails.getAuthorities()
        );

        ResponseCookie springCookie = ResponseCookie.from("JWT", jwt)
                .httpOnly(false)
                .secure(false)
                .path("/")
                .maxAge(60 * 1000)
                .domain("localhost")
                .build();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, springCookie.toString())
                .body(jwtResponse);

    }


    //    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @PostMapping
    public DataTablesOutput<AmznAccResult> findAllAmznAccountsPageable(@Valid @RequestBody DataTablesInput input) {
        return amznAccountService.findAll(input);
    }

    @GetMapping("/findAll")
    public ResponseEntity<?> findAllAmznAccounts() {
        List<AmznAccResult> amznAccResults = amznAccountService.findAll();
        return new ResponseEntity<>(amznAccResults, HttpStatus.OK);
    }

    //    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @PostMapping("/create")
    public ResponseEntity<?> createAmznAcc(@Validated @RequestBody AmznAccParam amznAccCreateParam) {
        AmznAccResult newAmznAcc = amznAccountService.create(amznAccCreateParam);
        return new ResponseEntity<>(newAmznAcc, HttpStatus.CREATED);
    }

    //    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @PostMapping("/import")
    public ResponseEntity<?> importNewAmznAcc(@RequestParam MultipartFile fileUploadAmznAcc) throws IOException {
        List<AmznAccResult> amznAccResultList = amznAccountService.importFile(fileUploadAmznAcc);
        return new ResponseEntity<>(amznAccResultList, HttpStatus.OK);
    }

    //    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAmznAcc(@RequestBody AmznAccParam amznAccParam, @PathVariable String id) {
        amznAccountService.update(id, amznAccParam);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteAmznAcc(@PathVariable String id) {
        amznAccountService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
