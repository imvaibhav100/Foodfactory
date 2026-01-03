package com.example.demo.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Component
public class DatabaseUrlConfig implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String databaseUrl = environment.getProperty("DATABASE_URL");
        
        if (databaseUrl == null) {
            databaseUrl = System.getenv("DATABASE_URL");
        }
        
        if (databaseUrl != null && !databaseUrl.isEmpty()) {
            try {
                // Parse Render's postgresql:// URL format
                URI dbUri = new URI(databaseUrl);
                
                String username = dbUri.getUserInfo().split(":")[0];
                String password = dbUri.getUserInfo().split(":")[1];
                String host = dbUri.getHost();
                int port = dbUri.getPort() != -1 ? dbUri.getPort() : 5432;
                String database = dbUri.getPath().substring(1); // Remove leading /
                
                String jdbcUrl = "jdbc:postgresql://" + host + ":" + port + "/" + database;
                
                // Create property map with higher priority
                Map<String, Object> properties = new HashMap<>();
                properties.put("spring.datasource.url", jdbcUrl);
                properties.put("spring.datasource.username", username);
                properties.put("spring.datasource.password", password);
                properties.put("spring.datasource.driverClassName", "org.postgresql.Driver");
                
                // Add as highest priority source
                MapPropertySource propertySource = new MapPropertySource("databaseUrlConfig", properties);
                environment.getPropertySources().addFirst(propertySource);
                
            } catch (Exception e) {
                System.err.println("Error parsing DATABASE_URL: " + e.getMessage());
            }
        }
    }
}


