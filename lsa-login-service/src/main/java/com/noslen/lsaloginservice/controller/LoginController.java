package com.noslen.lsaloginservice.controller;

import com.noslen.lsaloginservice.constants.WebResourceKeyConstants;
import com.noslen.lsaloginservice.requestDTO.LoginRequestDTO;
import com.noslen.lsaloginservice.service.LoginService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
import com.noslen.lsaloginservice.service.serviceImpl.LoginServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.ResponseEntity.ok;

@RestController
//@RequestMapping(value = WebResourceKeyConstants.BASE_API)
//@Api(value = "This is login controller")
public class LoginController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginServiceImpl.class);
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/api/login")
//    @ApiOperation(value = "This is login api", notes = "Request contains username and password")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequestDTO requestDTO, HttpServletRequest request) {

        String token = loginService.login(requestDTO, request);
        LOGGER.info(token);
        return ok().body(loginService.login(requestDTO, request));
    }

    @GetMapping("/test")
    public String test() {
        return "test done";
    }
}
