package org.example.aktanoopproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Разрешить CORS для всех путей
                        .allowedOrigins("*") // Разрешить запросы с любых доменов
                        .allowedMethods("*") // Разрешить все методы (GET, POST, PUT, DELETE и т.д.)
                        .allowedHeaders("*"); // Разрешить все заголовки
            }
        };
    }
}
