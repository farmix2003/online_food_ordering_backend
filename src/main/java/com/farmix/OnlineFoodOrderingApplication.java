package com.farmix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.farmix")
public class OnlineFoodOrderingApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineFoodOrderingApplication.class, args);
    }

}
