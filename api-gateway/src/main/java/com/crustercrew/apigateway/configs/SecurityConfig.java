package com.crustercrew.apigateway.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(exchange -> exchange
                        // 1. Endpoint Publik
                        .pathMatchers(
                                "/oauth2/**",
                                "/api/auth/**",
                                "/api/v1/auth/**",
                                "/login/**",
                                "/actuator/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        // 2. Registrasi user baru (Public)
                        .pathMatchers(HttpMethod.POST, "/api/v1/users", "/api/users").permitAll()
                        // 3. Role REQUESTER: Hanya boleh membuat Purchase Requisition
                        .pathMatchers(HttpMethod.POST, "/api/v1/requisitions", "/api/v1/requisitions/", "/api/v1/purchase-requisitions/**").hasAnyRole("REQUESTER", "ADMIN")
                        // 4. Role MANAGER: Review Purchase Requisition
                        .pathMatchers(HttpMethod.PUT, "/api/v1/requisitions/*/review", "/api/v1/purchase-requisitions/*/review").hasAnyRole("MANAGER", "FINANCE", "CEO", "ADMIN")
                        // 5. Semua endpoint lainnya wajib terotentikasi
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

        return http.build();
    }

    @Bean
    public ReactiveJwtAuthenticationConverterAdapter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<String> roles = jwt.getClaimAsStringList("roles");
            if (roles == null || roles.isEmpty()) {
                String singleRole = jwt.getClaimAsString("role");
                if (singleRole != null) {
                    roles = List.of(singleRole);
                } else {
                    return Collections.emptyList();
                }
            }
            return roles.stream()
                    .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        });
        return new ReactiveJwtAuthenticationConverterAdapter(jwtConverter);
    }
}