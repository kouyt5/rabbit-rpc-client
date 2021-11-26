package com.chenc.amqptest;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


// @SpringBootApplication(exclude = {RabbitAutoConfiguration.class})
@SpringBootApplication
public class AmqptestApplication {

    @Bean
    public CommandLineRunner usage(){
        return args -> {
            System.out.println("run usage");
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(AmqptestApplication.class, args);
    }

}
