package com.crustercrew.catalogservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(excludeName = {
        "org.springdoc.core.configuration.SpringDocDataRestConfiguration",
        "org.springdoc.core.configuration.SpringDocHateoasConfiguration"
})
public class CatalogServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CatalogServiceApplication.class, args);
    }

}
