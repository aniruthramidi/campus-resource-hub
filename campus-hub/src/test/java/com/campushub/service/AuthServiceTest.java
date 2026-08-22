package com.campushub.service;

import com.campushub.dto.AuthRequest;
import com.campushub.dto.AuthResponse;
import com.campushub.dto.RegisterRequest;
import com.campushub.entity.User;
import com.campushub.repository.UserRepository;
import com.campushub.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder()
                .userId(1L)
                .fullName("Alex Rivera")
                .email("alex@campus.edu")
                .passwordHash("encodedPassword")
                .department("Computer Science")
                .semester(6)
                .role("STUDENT")
                .build();
    }

    @Test
    void testRegisterSuccess() {
        RegisterRequest request = new RegisterRequest("Alex Rivera", "alex@campus.edu", "password123", "Computer Science", 6, "STUDENT");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);
        when(tokenProvider.generateToken(sampleUser.getEmail())).thenReturn("jwt-mock-token");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("jwt-mock-token", response.getToken());
        assertEquals("alex@campus.edu", response.getUser().getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testRegisterDuplicateEmailThrowsException() {
        RegisterRequest request = new RegisterRequest("Alex Rivera", "alex@campus.edu", "password123", "CS", 6, "STUDENT");
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authService.register(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testLoginSuccess() {
        AuthRequest request = new AuthRequest("alex@campus.edu", "password123");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(sampleUser));
        when(tokenProvider.generateToken(sampleUser.getEmail())).thenReturn("jwt-mock-token");

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("jwt-mock-token", response.getToken());
        assertEquals("Alex Rivera", response.getUser().getFullName());
    }
}
