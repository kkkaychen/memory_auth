package com.example.memory_auth_microservice.provider;

import com.example.memory_auth_microservice.model.MemEntity;
import com.example.memory_auth_microservice.service.MemberService;
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
public class MemberAuthenticationProvider implements AuthenticationProvider {
    private final MemberService memberService;
    @Autowired
    public MemberAuthenticationProvider(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        MemEntity member = memberService.getMember(username);

        if (member == null) { // || !memberService.validatePassword(member, password)
            throw new BadCredentialsException("Invalid member credentials");
        }

        return new UsernamePasswordAuthenticationToken(
                username, password, Collections.singleton(new SimpleGrantedAuthority("ROLE_MEMBER")));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return MemberAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
