package com.example.campusevent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CampusEventApplication {
    public static void main(String[] args) {
        SpringApplication.run(CampusEventApplication.class, args);
    }
}
