package com.crustercrew.authservice.integrations;

import com.crustercrew.authservice.dto.request.LoginRequest;
import com.crustercrew.authservice.dto.response.UserResponse;
import com.crustercrew.dto.APIResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service")
public interface UserClient {

    @PostMapping("/api/v1/users/verify-credentials")
    APIResponse<UserResponse> verifyCredentials(@RequestBody LoginRequest request);
}
