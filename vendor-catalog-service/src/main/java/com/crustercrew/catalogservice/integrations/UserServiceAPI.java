package com.crustercrew.catalogservice.integrations;

import com.crustercrew.catalogservice.dto.request.CreateUserRequest;
import com.crustercrew.catalogservice.dto.response.UserResponse;
import com.crustercrew.dto.APIResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service")
public interface UserServiceAPI {

    @GetMapping("/api/v1/users/{id}")
    APIResponse<UserResponse> getUserById(@PathVariable("id") Long id);

    @PostMapping("/api/v1/users")
    APIResponse<UserResponse> createUser(@RequestBody CreateUserRequest request);
}