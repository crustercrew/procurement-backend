package com.crustercrew.apigateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class UserContextGatewayFilter implements GlobalFilter, Ordered {

    static final String USER_ID = "X-User-Id";
    static final String USER_ROLE = "X-User-Role";
    static final String DEPARTMENT_ID = "X-Department-Id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
                .map(context -> context.getAuthentication())
                .filter(JwtAuthenticationToken.class::isInstance)
                .map(JwtAuthenticationToken.class::cast)
                .map(token -> withIdentityHeaders(exchange, token.getToken()))
                .defaultIfEmpty(stripIdentityHeaders(exchange))
                .flatMap(chain::filter);
    }

    private ServerWebExchange withIdentityHeaders(ServerWebExchange exchange, Jwt jwt) {
        ServerHttpRequest.Builder builder = exchange.getRequest().mutate()
                .headers(headers -> {
                    headers.remove(USER_ID);
                    headers.remove(USER_ROLE);
                    headers.remove(DEPARTMENT_ID);
                });
        String userId = claim(jwt, "userId");
        String role = claim(jwt, "role");
        String departmentId = claim(jwt, "departmentId");
        if (userId != null) {
            builder.header(USER_ID, userId);
        }
        if (role != null) {
            builder.header(USER_ROLE, role);
        }
        if (departmentId != null) {
            builder.header(DEPARTMENT_ID, departmentId);
        }
        return exchange.mutate().request(builder.build()).build();
    }

    private ServerWebExchange stripIdentityHeaders(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest().mutate()
                .headers(headers -> {
                    headers.remove(USER_ID);
                    headers.remove(USER_ROLE);
                    headers.remove(DEPARTMENT_ID);
                })
                .build();
        return exchange.mutate().request(request).build();
    }

    private String claim(Jwt jwt, String name) {
        Object value = jwt.getClaim(name);
        if (value == null) {
            return null;
        }
        String text = String.valueOf(value);
        return text.isBlank() ? null : text;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
