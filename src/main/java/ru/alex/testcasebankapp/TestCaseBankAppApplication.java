package ru.alex.testcasebankapp;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Collections;

@SpringBootApplication
public class TestCaseBankAppApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(TestCaseBankAppApplication.class);
        app.setDefaultProperties(Collections.singletonMap("spring.config.location", "classpath:/application-dev.yaml"));
        app.run(args);
    }

}
