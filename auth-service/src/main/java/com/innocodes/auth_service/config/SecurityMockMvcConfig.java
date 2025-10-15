//package com.innocodes.auth_service.config;
//
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Primary;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
//
//@TestConfiguration
//@EnableWebSecurity
//public class SecurityMockMvcConfig {
//
//    @Bean
//    @Primary
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf
//                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                        .ignoringRequestMatchers("/api/v1/auth/**")  // Disable CSRF for auth endpoints
//                )
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/v1/auth/login").permitAll()
//                        .requestMatchers("/api/v1/auth/register").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//
//        return http.build();
//    }
//}