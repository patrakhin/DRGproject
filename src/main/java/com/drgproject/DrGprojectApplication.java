package com.drgproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
/*@EntityScan(basePackages = {"com.drgproject.entity"})
@EnableJpaRepositories(basePackages = {"com.drgproject.repository"})*/
public class DrGprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(DrGprojectApplication.class, args);
    }

}
