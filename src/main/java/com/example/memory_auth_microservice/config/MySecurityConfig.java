package com.example.memory_auth_microservice.config;

import com.example.memory_auth_microservice.filter.JwtRequestFilter;
import com.example.memory_auth_microservice.provider.EmployeeAuthenticationProvider;
import com.example.memory_auth_microservice.provider.MemberAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class MySecurityConfig {
    private final JwtRequestFilter jwtRequestFilter;
    private final MemberAuthenticationProvider memberAuthenticationProvider;
    private final EmployeeAuthenticationProvider employeeAuthenticationProvider;

    @Autowired
    public MySecurityConfig(JwtRequestFilter jwtRequestFilter, MemberAuthenticationProvider memberAuthenticationProvider, EmployeeAuthenticationProvider employeeAuthenticationProvider) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.memberAuthenticationProvider = memberAuthenticationProvider;
        this.employeeAuthenticationProvider = employeeAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
        return NoOpPasswordEncoder.getInstance(); // 密碼沒加密
    }

    // 提供 AuthenticationManager 作為 Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(request -> request
                        .requestMatchers(HttpMethod.GET, "/**").permitAll()  // 允許 GET 請求無需 token
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()  // 允許 OPTIONS 請求無需 token
                        .requestMatchers("/auth/member", "/auth/emp").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // 無狀態
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)  // 添加 JWT 過濾器
                .build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000","http://localhost:3001"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(memberAuthenticationProvider)
                .authenticationProvider(employeeAuthenticationProvider)
                .build();
    }
}
