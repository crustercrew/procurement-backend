package com.crustercrew.requisitionservice.client;

import com.crustercrew.requisitionservice.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="user-service")
public interface UserFeignClient {
    @GetMapping("/users/{id}")
    UserResponse getUserById(@PathVariable("id") Long id);
}
