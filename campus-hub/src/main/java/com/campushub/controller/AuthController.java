package com.campushub.controller;

import com.campushub.dto.AuthRequest;
import com.campushub.dto.AuthResponse;
import com.campushub.dto.RegisterRequest;
import com.campushub.dto.UserDto;
import com.campushub.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication & Profile", description = "Endpoints for user registration, authentication, and profile management")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new student account", description = "Creates a new user account with student credentials and returns a JWT token")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(authService.getCurrentUserDto(authentication.getName()));
    }

    @GetMapping("/profile")
    public ResponseEntity<com.campushub.dto.UserProfileDto> getUserProfile(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(authService.getUserProfile(authentication.getName()));
    }

    @PutMapping("/profile")
    public ResponseEntity<com.campushub.dto.UserProfileDto> updateUserProfile(
            Authentication authentication,
            @Valid @RequestBody com.campushub.dto.UpdateProfileRequest request) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(authService.updateUserProfile(authentication.getName(), request));
    }
}
