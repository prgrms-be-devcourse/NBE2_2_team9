package com.mednine.pillbuddy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PillBuddyApplication {

    public static void main(String[] args) {
        SpringApplication.run(PillBuddyApplication.class, args);
    }
}
