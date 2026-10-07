package com.crustercrew.userservice.dto.response;

import com.crustercrew.userservice.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    @Builder.Default
    Long id = 0L;
    @Builder.Default
    String email = "";
    @Builder.Default
    String fullName = "";
    @Builder.Default
    String role = "";
    @Builder.Default
    Long departmentId = null;
    @Builder.Default
    Boolean isActive = false;
    @Builder.Default
    String createdAt = null;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole() != null ? user.getRole().toString() : null)
                .departmentId(user.getDepartmentId() != null ? user.getDepartmentId() : null)
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt() != null ? user.getCreatedAt().toString() : null)
                .build();
    }
}
