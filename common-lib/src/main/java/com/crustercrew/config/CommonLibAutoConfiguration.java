package com.crustercrew.config;

import com.crustercrew.exception.GlobalExceptionHandler;
import com.crustercrew.exception.SecurityExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.AccessDeniedException;

/**
 * Registers common-lib beans into consuming (servlet) services.
 * Without this, the advice classes live in packages the services do not component-scan.
 */
@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Import(GlobalExceptionHandler.class)
public class CommonLibAutoConfiguration {

    /**
     * Only registered when Spring Security is on the classpath; otherwise loading
     * {@link SecurityExceptionHandler} would fail with NoClassDefFoundError.
     */
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(AccessDeniedException.class)
    static class SecurityAdviceConfiguration {

        @Bean
        SecurityExceptionHandler securityExceptionHandler() {
            return new SecurityExceptionHandler();
        }
    }
}
