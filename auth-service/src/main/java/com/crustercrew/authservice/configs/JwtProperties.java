package com.crustercrew.authservice.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private long accessExpiration = 900000L;
    private long refreshExpiration = 604800000L;
}
