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
                .requestMatchers("/v0.5/**").permitAll()
                .requestMatchers("/api/v1/auth/authenticate")
                .permitAll()
                .requestMatchers("/api/v1/auth/register")
                .hasAuthority(ADMIN.name())
                .requestMatchers("/api/receptionist/patients/**")
                .hasAnyAuthority(RECEPTIONIST.name())
                .requestMatchers( "/api/v3/consent/**")
                .hasAnyAuthority(RECEPTIONIST.name())
                .requestMatchers("/api/receptionist/overview")
                .hasAnyAuthority(RECEPTIONIST.name())
                .requestMatchers("/api/v3/hip/**").permitAll()
                .requestMatchers("/api/v3/hiu/**").permitAll()
                .requestMatchers("/api/v1/doctor/**").hasAnyAuthority(ADMIN.name(),DOCTOR.name())
                .requestMatchers(GET,"/api/v1/doctor/**")
                .hasAnyAuthority(ADMIN_READ.name(),DOCTOR_READ.name())
                .requestMatchers(POST,"/api/v1/doctor/**").hasAnyAuthority(ADMIN_CREATE.name())
                .requestMatchers(DELETE,"/api/v1/doctor/**").hasAnyAuthority(ADMIN_DELETE.name())
                .requestMatchers(PUT,"/api/v1/doctor/**").hasAnyAuthority(ADMIN_UPDATE.name())
                .requestMatchers(GET, "/api/abdm/getProfile").hasAnyAuthority(RECEPTIONIST.name())
                .requestMatchers(POST, "/api/abdm/**").hasAnyAuthority(RECEPTIONIST.name())
                .requestMatchers("/api/v1/reception/**").hasAnyAuthority(ADMIN.name(),RECEPTIONIST.name())
                .requestMatchers(GET,"/api/v1/reception/**")
                .hasAnyAuthority(ADMIN_READ.name(),RECEPTIONIST_READ.name())
                .requestMatchers(POST,"/api/v1/reception/**").hasAnyAuthority(ADMIN_CREATE.name())
                .requestMatchers(DELETE,"/api/v1/reception/**").hasAnyAuthority(ADMIN_DELETE.name())
                .requestMatchers(PUT,"/api/v1/reception/**").hasAnyAuthority(ADMIN_UPDATE.name())
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
