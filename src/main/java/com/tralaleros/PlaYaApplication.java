package com.tralaleros;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.tralaleros"})
public class PlaYaApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlaYaApplication.class, args);
    }

}
