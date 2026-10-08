package com.crustercrew.catalogservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(excludeName = {
        "org.springdoc.core.configuration.SpringDocDataRestConfiguration",
        "org.springdoc.core.configuration.SpringDocHateoasConfiguration"
})
@EnableFeignClients
public class CatalogServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CatalogServiceApplication.class, args);
    }

}
