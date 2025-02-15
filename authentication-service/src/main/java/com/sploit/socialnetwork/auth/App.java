package com.sploit.socialnetwork.auth;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.Objects;

@SpringBootApplication
@EnableFeignClients
public class App {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("DATABASE_USER", Objects.requireNonNull(dotenv.get("DATABASE_USER")));
        System.setProperty("DATABASE_PASSWORD", Objects.requireNonNull(dotenv.get("DATABASE_PASSWORD")));
        System.setProperty("DATABASE_URL", Objects.requireNonNull(dotenv.get("DATABASE_URL")));
        SpringApplication.run(App.class, args);
    }
}
