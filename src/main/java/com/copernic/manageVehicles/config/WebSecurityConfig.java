
package com.copernic.manageVehicles.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/*Classe de configuració de Spring Security per configurar l'accés d'usuaris (autenticació).
 *Aquesta classe ha d'extendre de la classe WebSecurityConfigurerAdapter de Spring Security per poder
 *autenticar els usuaaris.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;

    @Autowired
    public void autenticacio(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.authorizeHttpRequests(authorize -> authorize
         
        
        .requestMatchers("/deleteUser/*", "/tasks/edit/*","/task-form", "/tasks/delete/*").hasRole("ADMINISTRADOR")
                
        .requestMatchers("/repairs/edit/*", "/repairs/delete/*","/repair-form/*","/repairs/view/*","/repairs/viewByNumberPlate/*",
                "/updateVehicle/*","/vehicle/*", "/vehicles", "/deleteVehicle/*",               
                "/users","/vehicle", "/tasks","/repairs")            
                .hasAnyRole("ADMINISTRADOR", "MECANICO")         
                
        
        .requestMatchers("/updateUser/*").hasAnyRole("ADMINISTRADOR", "USUARIO")
                
        .requestMatchers("/signin", "/login", "/error", "/error404", "/repairs/view/*", "/users/{nif}", "/logout", "/viewVehicle/*").permitAll()
        .anyRequest().authenticated())
                
                
        .formLogin(formLogin -> formLogin
            .successHandler(successHandler)) // Aquí se configura el AuthenticationSuccessHandler personalizado
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
