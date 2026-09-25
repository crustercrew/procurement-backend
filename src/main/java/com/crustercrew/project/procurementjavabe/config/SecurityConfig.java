package com.crustercrew.project.procurementjavabe.config;

import com.crustercrew.project.procurementjavabe.service.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties(JwtProperties.class)
@AllArgsConstructor
public class SecurityConfig {
    private final JwtService jwtService;

}
