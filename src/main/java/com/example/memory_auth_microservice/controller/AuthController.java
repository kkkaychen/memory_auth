package com.example.memory_auth_microservice.controller;

import com.example.memory_auth_microservice.model.JwtRes;
import com.example.memory_auth_microservice.model.UserNameAndPwd;
import com.example.memory_auth_microservice.service.MyUserDetailService;
import com.example.memory_auth_microservice.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final MyUserDetailService myUserDetailService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, MyUserDetailService myUserDetailService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.myUserDetailService = myUserDetailService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody UserNameAndPwd userNameAndPwd) {
        // Spring Security 身份驗證
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userNameAndPwd.userName(),
                userNameAndPwd.password()
        ));

        // 根據使用者帳號查詢資料
        final UserDetails userDetails = myUserDetailService.loadUserByUsername(userNameAndPwd.userName());

        // 產生 jwt
        final String jwt = jwtTokenUtil.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(new JwtRes(jwt));

    }
}
