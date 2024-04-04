package com.example.backend.Config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static com.example.backend.Entities.Permission.*;
import static com.example.backend.Entities.Role.*;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/auth/**")
                .permitAll()
                .requestMatchers("/api/v1/doctor/**").hasAnyAuthority(ADMIN.name(),DOCTOR.name())
                .requestMatchers(GET,"/api/v1/doctor/**")
                .hasAnyAuthority(ADMIN_READ.name(),DOCTOR_READ.name())
                .requestMatchers(POST,"/api/v1/doctor/**").hasAnyAuthority(ADMIN_CREATE.name())
                .requestMatchers(DELETE,"/api/v1/doctor/**").hasAnyAuthority(ADMIN_DELETE.name())
                .requestMatchers(PUT,"/api/v1/doctor/**").hasAnyAuthority(ADMIN_UPDATE.name())
                .requestMatchers("/api/admin/**").hasAuthority(ADMIN.name())
//                .requestMatchers("/api/admin/**").hasRole(ADMIN.name())
                .requestMatchers(GET,"/api/admin/**").hasAnyAuthority(ADMIN_READ.name(),DOCTOR_READ.name())
                .requestMatchers(POST,"/api/admin/**").hasAnyAuthority(ADMIN_CREATE.name(),DOCTOR_CREATE.name())
                .requestMatchers(DELETE,"/api/admin/**").hasAnyAuthority(ADMIN_DELETE.name(),DOCTOR_DELETE.name())
                .requestMatchers(PUT,"/api/admin/**").hasAnyAuthority(ADMIN_UPDATE.name(),DOCTOR_UPDATE.name())
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl("/api/v1/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler(((request, response, authentication) -> SecurityContextHolder.clearContext()))
        ;
        return http.build();
    }
}
