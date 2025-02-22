package com.g44.kodeholik.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.g44.kodeholik.exception.ForbiddenException;
import com.g44.kodeholik.handler.JwtAuthenticationFailureHandler;
import com.g44.kodeholik.repository.user.UserRepository;
import com.g44.kodeholik.service.token.TokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    private final UserDetailsService userDetailsService;

    private final UserRepository userRepository;

    private final JwtAuthenticationFailureHandler authenticationFailureHandler;

    private static List<String> skipFilterUrls = Arrays.asList(
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
            "/api/v1/problem/compile-information/**",
            "/api/v1/course/list/**",
            "/api/v1/course/detail/**",
            "/api/v1/course/search/**");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String accessToken = null;

        String username = null;
        // Lấy token từ Cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("access_token")) {
                    accessToken = cookie.getValue();
                    username = tokenService.extractUsername(accessToken);
                }
            }
        }
        log.info("access: " + accessToken + " " + request.getRequestURI());
        if (accessToken != null && !accessToken.equals("")) {
            if ((username != null && !username.equals("")) &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (userRepository.existsByUsernameOrEmail(username).isPresent()
                        && tokenService.validateToken(accessToken)) {
                    if (userRepository.isUserNotAllowed(username)) {
                        throw new ForbiddenException("This account is not allowed to do this action",
                                "This account is not allowed to do this action");
                    }
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } else {
                    authenticationFailureHandler.onAuthenticationFailure(request, response,
                            new AuthenticationException("Invalid or expired JWT token") {
                            });
                    return;
                }
            }
        } else {
            authenticationFailureHandler.onAuthenticationFailure(request, response,
                    new AuthenticationException("Invalid or expired JWT token") {
                    });
            return;
        }
        filterChain.doFilter(request, response);

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        if (request.getMethod().equals(HttpMethod.OPTIONS.toString())) {
            log.info("OPTIONS");
            return true;
        }

        Cookie[] cookies = request.getCookies();
        if (!request.getRequestURI().equals("/api/v1/auth/login/oauth2/google")) {
            String accessToken = "";
            String username = "";
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("access_token")) {
                        accessToken = cookie.getValue();
                        username = tokenService.extractUsername(accessToken);
                    }
                }
            }
            log.info("access: " + accessToken);
            if (accessToken != null && !accessToken.equals("")) {
                if ((username != null && !username.equals("")) &&
                        SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    if (userRepository.existsByUsernameOrEmail(username).isPresent()
                            && tokenService.validateToken(accessToken)) {
                        if (userRepository.isUserNotAllowed(username)) {
                            throw new ForbiddenException("This account is not allowed to do this action",
                                    "This account is not allowed to do this action");
                        }
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            }
        }
        return skipFilterUrls.stream().anyMatch(url -> new AntPathRequestMatcher(url).matches(request));
    }

}
