package com.g44.kodeholik.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Value("${spring.application.url}")
    private String applicationUrl;

    @Value("${spring.application.fe-url}")
    private String frontendUrl;

    @Value("${spring.application.admin-fe-url}")
    private String adminFrontendUrl;

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // configuration.setAllowedOrigins(Arrays.asList("http://localhost:5174")); //
        // Domain của client
        configuration.setAllowedOrigins(Arrays.asList("http://localhost", "http://localhost:81"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*")); // Cho phép tất cả headers
        configuration.setAllowCredentials(true); // Cho phép credentials

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // @Bean
    // public CorsFilter corsFilter() {
    // CorsConfiguration corsConfiguration = new CorsConfiguration();
    // corsConfiguration.setAllowCredentials(true);
    // corsConfiguration.setAllowedOrigins(Arrays.asList(frontendUrl,
    // applicationUrl));
    // corsConfiguration.setAllowedHeaders(Arrays.asList("Origin",
    // "Access-Control-Allow-Origin", "Content-Type",
    // "Accept", "Authorization", "Origin, Accept", "X-Requested-With",
    // "Access-Control-Request-Method",
    // "Access-Control-Request-Headers"));
    // corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type",
    // "Accept", "Authorization",
    // "Access-Control-Allow-Origin", "Access-Control-Allow-Origin",
    // "Access-Control-Allow-Credentials"));
    // corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT",
    // "PATCH", "DELETE", "OPTIONS"));
    // UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new
    // UrlBasedCorsConfigurationSource();
    // urlBasedCorsConfigurationSource.registerCorsConfiguration("/**",
    // corsConfiguration);
    // return new CorsFilter(urlBasedCorsConfigurationSource);
    // }

    // @Bean
    // public CorsFilter corsFilter() {
    // CorsConfiguration corsConfiguration = new CorsConfiguration();
    // corsConfiguration.setAllowCredentials(true); // ✅ Cho phép gửi cookie

    // corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:5174",
    // applicationUrl)); // ✅ Chỉ định
    // // frontend
    // corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT",
    // "DELETE", "OPTIONS"));

    // corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type",
    // "Accept", "Authorization",
    // "Access-Control-Allow-Origin", "Access-Control-Allow-Methods",
    // "Access-Control-Allow-Headers",
    // "Access-Control-Allow-Credentials")); // thiết

    // // corsConfiguration.setExposedHeaders(Arrays.asList("Authorization")); // ✅
    // Nếu
    // // cần expose token

    // UrlBasedCorsConfigurationSource source = new
    // UrlBasedCorsConfigurationSource();
    // source.registerCorsConfiguration("/**", corsConfiguration);

    // return new CorsFilter(source);
    // }

    // @Bean
    // public CorsFilter corsFilter() {
    // CorsConfiguration corsConfiguration = new CorsConfiguration();

    // // ✅ Cho phép gửi Cookie
    // corsConfiguration.setAllowCredentials(true);

    // // ✅ Cho phép frontend truy cập
    // corsConfiguration.setAllowedOriginPatterns(Arrays.asList("http://localhost:5174"));

    // // ✅ Cho phép các header cần thiết
    // corsConfiguration.setAllowedHeaders(Arrays.asList(
    // "Origin", "Content-Type", "Accept", "Authorization", "Cookie"));

    // // ✅ Expose headers cần thiết
    // corsConfiguration.setExposedHeaders(Arrays.asList("Authorization",
    // "Set-Cookie"));

    // // ✅ Cho phép tất cả các phương thức HTTP
    // corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT",
    // "PATCH", "DELETE", "OPTIONS"));

    // // ✅ Cho phép preflight request tồn tại lâu hơn (cache)
    // corsConfiguration.setMaxAge(3600L);

    // // ✅ Định nghĩa áp dụng cho toàn bộ API
    // UrlBasedCorsConfigurationSource source = new
    // UrlBasedCorsConfigurationSource();
    // source.registerCorsConfiguration("/**", corsConfiguration);

    // return new CorsFilter(source);
    // }

}