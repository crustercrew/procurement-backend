package com.crustercrew.userservice.controllers;

import com.crustercrew.userservice.dto.request.LoginRequest;
import com.crustercrew.userservice.dto.response.LoginResponse;
import com.crustercrew.userservice.entity.User;
import com.crustercrew.userservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthControllers {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
       User user = userRepository.findByUsername(request.getUsername())
               .orElseThrow(()-> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password"));

       if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
           throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
       }

        Instant now = Instant.now();
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuer("http://localhost:8081")
                .issuedAt(now)
                .expiresAt(now.plus(Duration.ofHours(1)))
                .subject(user.getUsername())
                .claim("username",user.getUsername())
                .claim("roles", List.of("ROLE_" + user.getRole().name()))
                .claim("scope","read write")
                .build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();

        return ResponseEntity.ok(
                LoginResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .username(user.getUsername())
                .role(user.getRole().name())
                .build()
        );
    }
}
