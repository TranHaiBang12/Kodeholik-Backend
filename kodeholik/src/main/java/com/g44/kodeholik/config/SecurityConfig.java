package com.g44.kodeholik.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
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
import com.g44.kodeholik.handler.CustomAccessDeniedHandler;
import com.g44.kodeholik.handler.CustomAuthenticationEntryPoint;
import com.g44.kodeholik.model.enums.user.UserRole;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final UserDetailsService userDetailsService;

        private final String[] publicUrls = {
                        "/login/**",
                        "/api/v1/lambda/**",
                        "/api/v1/auth/logout",
                        "/api/v1/auth/login",
                        "/api/v1/auth/login-admin",
                        "/api/v1/auth/login/**",
                        "/api/v1/problem/no-achieved-info",
                        "/api/v1/auth/reset-password-init",
                        "/api/v1/auth/reset-password-check",
                        "/api/v1/auth/reset-password-finish",
                        "/api/v1/auth/rotate-token",
                        "/api/v1/problem/search/**",
                        "/api/v1/problem/suggest/**",
                        "/api/v1/problem/description/**",
                        "/api/v1/problem/language-support/**",
                        "/api/v1/problem/compile-information/**",
                        "/api/v1/course/list/**",
                        "/api/v1/course/top-popular",
                        "/api/v1/openai/**",
                        "/api/v1/course/detail/**",
                        "/api/v1/course/search/**",
                        "/api/v1/course/rating/**",
                        "/api/v1/tag/all-skill/**",
                        "/api/v1/tag/all-topic/**",
                        "/api/v1/s3/presigned-url",
                        "/api/v1/exam/run/**",
                        "/ws",
                        "/ws/**"
        };

        private final String[] teacherUrls = {
                        "/api/v1/problem/add-problem",
                        "/api/v1/problem/edit-problem/**",
                        "/api/v1/problem/activate-problem/**",
                        "/api/v1/problem/deactivate-problem/**",
                        "/api/v1/problem/list-problem",
                        "/api/v1/course/add",
                        "/api/v1/course/update/**",
                        "/api/v1/course/overview-report",
                        "/api/v1/chapter/add",
                        "/api/v1/chapter/update/**",
                        "/api/v1/lesson/add",
                        "/api/v1/lesson/update/**",
                        "/api/v1/chapter/list-problem",
                        "/api/v1/problem/private-problem",
                        "/api/v1/problem/overview-report"
        };

        private final JwtFilter jwtFilter;

        private final SimpleUrlAuthenticationSuccessHandler onOauth2LoginSuccessHandler;

        private final MessageProperties messageProperties;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
                return httpSecurity
                                .cors(Customizer.withDefaults())
                                .csrf(customizer -> customizer.disable())
                                .authorizeHttpRequests(
                                                request -> request
                                                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                                                .requestMatchers("/api/v1/admin/**")
                                                                .hasAuthority(UserRole.ADMIN.toString())
                                                                .requestMatchers("/api/v1/examiner/**")
                                                                .hasAuthority(UserRole.EXAMINER.toString())
                                                                .requestMatchers(teacherUrls)
                                                                .hasAuthority(UserRole.TEACHER.toString())
                                                                .requestMatchers(publicUrls).permitAll()
                                                                .anyRequest()
                                                                .authenticated())
                                .authenticationProvider(authenticationProvider())

                                .oauth2Login(oauth2login -> {
                                        oauth2login
                                                        .successHandler(onOauth2LoginSuccessHandler);
                                })
                                .exceptionHandling(
                                                exception -> exception
                                                                .accessDeniedHandler(new CustomAccessDeniedHandler())
                                                                .authenticationEntryPoint(
                                                                                new CustomAuthenticationEntryPoint(
                                                                                                messageProperties)))
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
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
