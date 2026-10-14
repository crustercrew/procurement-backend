package com.crustercrew.security;

import com.crustercrew.enums.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class ServiceRequestContext {

    public static final String USER_ID_HEADER = "X-User-Id";
    public static final String USER_ROLE_HEADER = "X-User-Role";
    public static final String DEPARTMENT_ID_HEADER = "X-Department-Id";

    private final HttpServletRequest request;

    public ServiceRequestContext(HttpServletRequest request) {
        this.request = request;
    }

    public Long getUserId() {
        return parseLong(request.getHeader(USER_ID_HEADER), USER_ID_HEADER, true);
    }

    public UserRole getUserRole() {
        String role = request.getHeader(USER_ROLE_HEADER);
        if (role == null || role.isBlank()) {
            throw new IllegalStateException("Missing " + USER_ROLE_HEADER + " header");
        }
        String normalized = role.startsWith("ROLE_") ? role.substring("ROLE_".length()) : role;
        return UserRole.valueOf(normalized);
    }

    public Long getDepartmentId() {
        return parseLong(request.getHeader(DEPARTMENT_ID_HEADER), DEPARTMENT_ID_HEADER, false);
    }

    private Long parseLong(String value, String header, boolean required) {
        if (value == null || value.isBlank()) {
            if (required) {
                throw new IllegalStateException("Missing " + header + " header");
            }
            return null;
        }
        return Long.parseLong(value);
    }
}
