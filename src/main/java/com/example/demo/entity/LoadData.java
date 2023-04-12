package com.example.demo.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadData {

    private static final Logger log = LoggerFactory.getLogger(LoadData.class);

    @Bean
    CommandLineRunner initDatabase(CharacterRepository repository) {
        return args -> {
            log.info("Preloading" + repository.save(new Character("Frodo", "Baggins", "frodo@baggins.lotr")));
            log.info("Preloading" + repository.save(new Character("Bilbo", "Baggins", "bilbo@baggins.lotr")));
        };
    }
}

