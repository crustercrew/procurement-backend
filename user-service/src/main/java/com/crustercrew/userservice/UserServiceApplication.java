package com.crustercrew.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(excludeName = {
        "org.springdoc.core.configuration.SpringDocDataRestConfiguration",
        "org.springdoc.core.configuration.SpringDocHateoasConfiguration"
})
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

}