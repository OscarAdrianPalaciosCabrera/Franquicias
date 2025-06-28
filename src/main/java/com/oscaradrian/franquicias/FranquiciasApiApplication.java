package com.oscaradrian.franquicias;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories(basePackages = "com.oscaradrian.franquicias.infrastructure.repository")

public class FranquiciasApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(FranquiciasApiApplication.class, args);
    }
}
