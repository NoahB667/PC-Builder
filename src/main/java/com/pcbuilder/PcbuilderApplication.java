package com.pcbuilder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan({"com.pcbuilder.entity", "com.pcbuilder.user"}) // Scan for JPA entities
@EnableJpaRepositories({"com.pcbuilder.repository", "com.pcbuilder.user"}) // Scan for JPA repositories
public class PcbuilderApplication {
    public static void main(String[] args) {
        SpringApplication.run(PcbuilderApplication.class, args);
    }
}
