package com.crustercrew.apigateway.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.
                csrf(csrf -> csrf.disable())
                .authorizeExchange(exchange -> exchange
                        // 1. Endpoint Publik (Bisa diakses tanpa token)
                        .pathMatchers("/oauth2/**", "/api/auth/**", "/login/**", "/actuator/**").permitAll()
                        // 2. Role REQUESTER: Hanya boleh membuat Purchase Requisition
                        .pathMatchers(HttpMethod.POST, "/api/requisitions", "/api/requisitions/").hasRole("REQUESTER")
                        // 3. Role MANAGER: Hanya boleh me-review (Approve/Reject) Purchase Requisition
                        .pathMatchers(HttpMethod.PUT, "/api/requisitions/*/review").hasRole("MANAGER")
                        // 4. Semua endpoint lainnya wajib login (misal GET /api/requisitions)
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

        return http.build();
    }

    // Mengubah claim "roles" dari JWT menjadi GrantedAuthority (ROLE_REQUESTER, ROLE_MANAGER)
    @Bean
    public ReactiveJwtAuthenticationConverterAdapter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<String> roles = jwt.getClaimAsStringList("roles");
            if (roles == null || roles.isEmpty()) {
                return Collections.emptyList();
            }
            return roles.stream()
                    .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        });
        return new ReactiveJwtAuthenticationConverterAdapter(jwtConverter);
    }
}
