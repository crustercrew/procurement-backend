package com.crustercrew.requisitionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class RequisitionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RequisitionServiceApplication.class, args);
    }

}
