package com.example.memory_auth_microservice.provider;

import com.example.memory_auth_microservice.model.EmpEntity;
import com.example.memory_auth_microservice.model.MemEntity;
import com.example.memory_auth_microservice.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class EmployeeAuthenticationProvider implements AuthenticationProvider {
    private final EmpService empService;

    @Autowired
    public EmployeeAuthenticationProvider(EmpService empService) {
        this.empService = empService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        EmpEntity emp = empService.getEmp(username);

        if (emp == null) { // || !empService.validatePassword(member, password)
            throw new BadCredentialsException("Invalid emp credentials");
        }

        return new UsernamePasswordAuthenticationToken(
                username, password, Collections.singleton(new SimpleGrantedAuthority("ROLE_EMPLOYEE")));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return EmployeeAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
