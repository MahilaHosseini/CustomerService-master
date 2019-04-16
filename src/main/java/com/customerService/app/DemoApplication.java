package com.customerService.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class DemoApplication {
    private static Logger logger =   LoggerFactory.getLogger(DemoApplication.class);

    public static void main(String[] args) {
        logger.info("main started!");
        SpringApplication.run(DemoApplication.class, args);
        logger.info("end main");
    }

}
