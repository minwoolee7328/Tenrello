package com.example.tenrello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TenrelloApplication {

    public static void main(String[] args) {
        SpringApplication.run(TenrelloApplication.class, args);
    }

}
