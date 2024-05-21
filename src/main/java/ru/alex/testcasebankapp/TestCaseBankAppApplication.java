package ru.alex.testcasebankapp;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Collections;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class TestCaseBankAppApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(TestCaseBankAppApplication.class);
        app.setDefaultProperties(Collections.singletonMap("spring.config.location", "classpath:/application.yaml"));
        app.run(args);
    }

}
