/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.copernic.manageVehicles.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;

/**
 *
 * @author vigob
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
    
    @Bean
    public DefaultSecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity.authorizeHttpRequests(authorize -> authorize
        .requestMatchers("/session").authenticated()
        .anyRequest().permitAll())
        .formLogin(formLogin -> formLogin
            .defaultSuccessUrl("/session", true))
            .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                    .invalidSessionUrl("/login").maximumSessions(1)
                    .expiredUrl("/login")
                    .sessionRegistry(sessionRegistry())
                    .and().sessionFixation().migrateSession())
        .build();
}
    
    @Bean
    public SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }
    
}
