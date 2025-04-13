package com.sploit.socialnetwork.auth.config;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DotenvEnvironmentalLoaderConfiguration {

    private final Dotenv dotenv;

    public DotenvEnvironmentalLoaderConfiguration() {
        this.dotenv = Dotenv.configure().load();
    }

    @PostConstruct
    public void configureEnvironment() {
        dotenv.entries().forEach(entry -> {
            System.setProperty(entry.getKey(), entry.getValue());
        });
    }
}
