package com.mytransport.config;

import com.mytransport.services.CarUserService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
public class SecurityConfig {

    @Value("${app.admin.username:admin}")
    private String adminUsername;

    @Value("${app.admin.password:change-me}")
    private String adminPassword;

    private final CarUserService carUserService;

    public SecurityConfig(CarUserService carUserService) {
        this.carUserService = carUserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // /static/**, /public/**, /resources/**, /META-INF/resources/**, /webjars/**
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/", "/about", "/services", "/process", "/prices",
                                "/calculator", "/contact", "/v/**", "/error").permitAll()
                        .requestMatchers("/carlog/admin/**").hasRole("ADMIN")
                        .requestMatchers("/carlog/**").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login.defaultSuccessUrl("/carlog", false))
                .logout(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        return username -> {
            if (adminUsername.equals(username)) {
                UserDetails admin = User.builder()
                        .username(adminUsername)
                        .password(passwordEncoder.encode(adminPassword))
                        .roles("ADMIN")
                        .build();
                return admin;
            }

            return carUserService.loadDatabaseUser(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found."));
        };
    }
}
