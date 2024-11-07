package com.example.memory_auth_microservice.provider;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class EmployeeAuthenticationToken extends UsernamePasswordAuthenticationToken {
    public EmployeeAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }
}
