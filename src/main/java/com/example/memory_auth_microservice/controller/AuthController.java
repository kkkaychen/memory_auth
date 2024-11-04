package com.example.memory_auth_microservice.controller;


import com.example.memory_auth_microservice.model.JwtRes;
import com.example.memory_auth_microservice.model.UserNameAndPwd;
import com.example.memory_auth_microservice.service.MyUserDetailService;
import com.example.memory_auth_microservice.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseCookie;


@RestController
@Slf4j
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
    public ResponseEntity<JwtRes> authenticate(@RequestBody UserNameAndPwd userNameAndPwd) {
        // Spring Security 身份驗證
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userNameAndPwd.userName(),
                userNameAndPwd.password()
        ));

        // 根據使用者帳號查詢資料
        final UserDetails userDetails = myUserDetailService.loadUserByUsername(userNameAndPwd.userName());

        // 產生 access token
        String accessToken = jwtTokenUtil.generateAccessToken(userDetails.getUsername());

        // 產生 refresh token
        String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails.getUsername());

        // 設置 Cookie 返回給前端
        ResponseCookie accessTokenCookie = ResponseCookie.from("access_token", accessToken)
                .httpOnly(false)
                .path("/")
                .maxAge(900) // 15分鐘
                .build();


        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true) // 建議在 HTTPS 下使用
                .path("/")
                .maxAge(604800) // 7 天
                .build();


        JwtRes jwtRes = new JwtRes();
        jwtRes.setAccessToken(accessToken);
        jwtRes.setRefreshToken(refreshToken);

        return ResponseEntity.ok()
                .header("Set-Cookie", accessTokenCookie.toString())
                .header("Set-Cookie", refreshTokenCookie.toString())
                .body(jwtRes);

    }
}
