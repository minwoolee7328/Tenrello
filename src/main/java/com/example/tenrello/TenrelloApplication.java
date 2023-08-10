package com.example.tenrello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
@EnableScheduling      // 스케줄러 기능 활성화
@EnableJpaAuditing
@SpringBootApplication
public class TenrelloApplication {

    public static void main(String[] args) {
        SpringApplication.run(TenrelloApplication.class, args);
    }

}
