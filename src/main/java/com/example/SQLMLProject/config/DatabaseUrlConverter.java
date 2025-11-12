//package com.example.SQLMLProject.config;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.env.EnvironmentPostProcessor;
//import org.springframework.core.env.ConfigurableEnvironment;
//import org.springframework.core.env.MapPropertySource;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Converts DigitalOcean's DATABASE_URL format to JDBC format.
// * DigitalOcean provides: postgresql://user:pass@host:port/db?sslmode=require
// * We need: jdbc:postgresql://user:pass@host:port/db?sslmode=require
// */
//public class DatabaseUrlConverter implements EnvironmentPostProcessor {
//
//    @Override
//    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
//        String databaseUrl = environment.getProperty("DATABASE_URL");
//
//        if (databaseUrl != null && !databaseUrl.isEmpty() && !databaseUrl.startsWith("jdbc:")) {
//            // Convert postgresql:// to jdbc:postgresql://
//            String jdbcUrl = "jdbc:" + databaseUrl;
//
//            Map<String, Object> map = new HashMap<>();
//            map.put("spring.datasource.url", jdbcUrl);
//
//            MapPropertySource propertySource = new MapPropertySource("databaseUrlConverter", map);
//            environment.getPropertySources().addFirst(propertySource);
//        }
//    }
//}
