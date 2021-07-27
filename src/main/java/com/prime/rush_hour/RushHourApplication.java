package com.prime.rush_hour;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RushHourApplication {

    public static void main(String[] args) {

        try {
            SpringApplication.run(RushHourApplication.class, args);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

}
