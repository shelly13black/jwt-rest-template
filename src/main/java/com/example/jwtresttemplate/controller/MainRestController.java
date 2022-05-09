package com.example.jwtresttemplate.controller;

import com.example.jwtresttemplate.jwt.config.JwtTokenUtil;
import com.example.jwtresttemplate.jwt.model.ApiRefreshTokenRequest;
import com.example.jwtresttemplate.jwt.model.ApiToken;
import com.example.jwtresttemplate.jwt.model.ApiTokenRequest;
import com.example.jwtresttemplate.service.MainService;
import io.swagger.annotations.Api;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Api(value = "/jrt", tags = {"JWT_RestTemplate"})
public class MainRestController {

    private static final Logger LOGGER = LogManager.getLogger(MainRestController.class);
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private MainService mainService;

    @PostMapping("/jwt-rest-template/auth/get-token")
    public ResponseEntity<?> getToken(@RequestBody ApiTokenRequest tokenRequest){
        final String token = jwtTokenUtil.encodeJWTKey(tokenRequest);
        return ResponseEntity.ok(new ApiToken(token));
    }

    @PostMapping("/jwt-rest-template/auth/refresh-token")
    public ResponseEntity<?> getRefreshToken(@RequestBody ApiRefreshTokenRequest refreshTokenRequest) throws Exception{
        final String token = jwtTokenUtil.generateRefreshToken(refreshTokenRequest);
        return ResponseEntity.ok(new ApiToken(token));
    }

    @PostMapping("/jwt-rest-template/test")
    public ResponseEntity<String> test(){
        return new ResponseEntity<>(mainService.test(), HttpStatus.OK);
    }
}
