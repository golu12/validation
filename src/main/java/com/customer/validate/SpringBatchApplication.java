package com.customer.validate;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
@EnableAutoConfiguration
public class SpringBatchApplication {

   public  static void main(String args[]){
        SpringApplication.run(SpringBatchApplication.class, args);

    }
}