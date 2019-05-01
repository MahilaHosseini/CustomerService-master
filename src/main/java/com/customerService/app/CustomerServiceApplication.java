package com.customerService.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class CustomerServiceApplication {
    private static Logger logger =   LoggerFactory.getLogger(CustomerServiceApplication.class);

    public static void main(String[] args) {
        logger.info("main started!");
        SpringApplication.run(CustomerServiceApplication.class, args);
        logger.info("end main");
    }

}
