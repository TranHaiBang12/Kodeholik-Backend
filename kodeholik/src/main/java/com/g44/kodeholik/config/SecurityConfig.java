package com.g44.kodeholik.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.g44.kodeholik.filter.JwtFilter;
import com.g44.kodeholik.handler.CustomAuthenticationEntryPoint;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    private final String[] publicUrls = {
            "/login/**",
            "/api/v1/auth/login",
            "/api/v1/auth/login/**",
            "/api/v1/problem/no-achieved-info",
            "/api/v1/auth/reset-password-init",
            "/api/v1/auth/reset-password-check",
            "/api/v1/auth/reset-password-finish",
            "/api/v1/auth/rotate-token",
            "/api/v1/problem/search/**",
            "/api/v1/problem/suggest/**",
            "/api/v1/problem/description/**",
            "/api/v1/problem/compile-information/**"
    };

    private final JwtFilter jwtFilter;

    private final SimpleUrlAuthenticationSuccessHandler onOauth2LoginSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(customizer -> customizer.disable())
                .authorizeHttpRequests(
                        request -> request
                                .requestMatchers(publicUrls).permitAll()
                                .anyRequest()
                                .authenticated())
                .authenticationProvider(authenticationProvider())

                .oauth2Login(oauth2login -> {
                    oauth2login
                            .successHandler(onOauth2LoginSuccessHandler);
                })
                .exceptionHandling(
                        exception -> exception.authenticationEntryPoint(new CustomAuthenticationEntryPoint()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(10));
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
