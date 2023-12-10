package dev.Innocent.EventManagementSystem.configuration;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;


    @Configuration
    @Primary
    public class CorsConfig implements CorsConfigurationSource {

        @Override
        public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
            CorsConfiguration config = new CorsConfiguration();
            config.addAllowedOrigin("https://event-booking-frontend-squad16.netlify.app");
            config.addAllowedOrigin("http://localhost:5173/");
            config.addAllowedMethod("*");
            config.addAllowedHeader("*");
            config.setAllowCredentials(true);
            return config;
        }
    }


