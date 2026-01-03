package com.example.demo.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.net.URI;

@Component
public class DatabaseUrlConfig implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        String databaseUrl = System.getenv("DATABASE_URL");
        
        if (databaseUrl != null && !databaseUrl.isEmpty()) {
            // Parse Render's postgresql:// URL and set Spring properties
            URI dbUri = new URI(databaseUrl);
            
            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String host = dbUri.getHost();
            int port = dbUri.getPort() != -1 ? dbUri.getPort() : 5432;
            String database = dbUri.getPath().substring(1); // Remove leading /
            
            // Set system properties for Spring Boot to use
            System.setProperty("spring.datasource.url", "jdbc:postgresql://" + host + ":" + port + "/" + database);
            System.setProperty("spring.datasource.username", username);
            System.setProperty("spring.datasource.password", password);
        }
    }
}

