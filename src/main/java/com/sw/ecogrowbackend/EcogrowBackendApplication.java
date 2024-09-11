package com.sw.ecogrowbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class EcogrowBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcogrowBackendApplication.class, args);
    }

}
