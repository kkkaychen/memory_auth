package com.example.memory_auth_microservice.provider;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class MemberAuthenticationToken extends UsernamePasswordAuthenticationToken {
    public MemberAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }
}
