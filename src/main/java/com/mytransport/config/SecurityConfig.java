package com.mytransport.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Value("${app.admin.username:admin}")
    private String adminUsername;

    @Value("${app.admin.password:change-me}")
    private String adminPassword;

    public SecurityConfig() {
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // /static/**, /public/**, /resources/**, /META-INF/resources/**, /webjars/**
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/", "/about", "/services", "/process", "/prices",
                                "/calculator", "/contact", "/v/**", "/error", "/login").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/", false)
                        .permitAll()
                )
                .logout(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public org.springframework.security.core.userdetails.UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        return username -> org.springframework.security.core.userdetails.User.builder()
                .username(adminUsername)
                .password(passwordEncoder.encode(adminPassword))
                .roles("ADMIN")
                .build();
    }
}
