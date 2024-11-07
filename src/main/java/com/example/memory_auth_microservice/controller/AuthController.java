package com.example.memory_auth_microservice.controller;


import com.example.memory_auth_microservice.model.JwtRes;
import com.example.memory_auth_microservice.model.UserNameAndPwd;
import com.example.memory_auth_microservice.provider.EmployeeAuthenticationToken;
import com.example.memory_auth_microservice.provider.MemberAuthenticationToken;
import com.example.memory_auth_microservice.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }
    @PostMapping("/member")
    public ResponseEntity<JwtRes> memberLogin(@RequestBody UserNameAndPwd userNameAndPwd) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new MemberAuthenticationToken(userNameAndPwd.userName(), userNameAndPwd.password()
                    ));

            // 產生 access token
            String accessToken = jwtTokenUtil.generateAccessToken(userNameAndPwd.userName(), "ROLE_MEMBER");
            log.info("accessToken: " + accessToken);
            // 產生 refresh token
            String refreshToken = jwtTokenUtil.generateRefreshToken(userNameAndPwd.userName());
            log.info("refreshToken: " + refreshToken);

            ResponseCookie accessTokenCookie = ResponseCookie.from("member_access_token", accessToken)
                    .httpOnly(false)
                    .path("/")
                    .maxAge(900) // 15分鐘
                    .build();


            ResponseCookie refreshTokenCookie = ResponseCookie.from("member_refresh_token", refreshToken)
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
        } catch (AuthenticationException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new JwtRes());
        }
    }

    @PostMapping("/emp")
    public ResponseEntity<JwtRes> empLogin(@RequestBody UserNameAndPwd userNameAndPwd) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new EmployeeAuthenticationToken(userNameAndPwd.userName(), userNameAndPwd.password()
            ));

            // 產生 access token
            String accessToken = jwtTokenUtil.generateAccessToken(userNameAndPwd.userName(), "ROLE_EMPLOYEE");
            log.info("accessToken: " + accessToken);
            // 產生 refresh token
            String refreshToken = jwtTokenUtil.generateRefreshToken(userNameAndPwd.userName());
            log.info("refreshToken: " + refreshToken);

            ResponseCookie accessTokenCookie = ResponseCookie.from("emp_access_token", accessToken)
                    .httpOnly(false)
                    .path("/")
                    .maxAge(900) // 15分鐘
                    .build();


            ResponseCookie refreshTokenCookie = ResponseCookie.from("emp_refresh_token", refreshToken)
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
        } catch (AuthenticationException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new JwtRes());
        }
    }

}
