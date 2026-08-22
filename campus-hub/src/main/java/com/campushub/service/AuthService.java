package com.campushub.service;

import com.campushub.dto.AuthRequest;
import com.campushub.dto.AuthResponse;
import com.campushub.dto.RegisterRequest;
import com.campushub.dto.UserDto;
import com.campushub.entity.User;
import com.campushub.repository.UserRepository;
import com.campushub.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email address is already registered: " + request.getEmail());
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .department(request.getDepartment())
                .semester(request.getSemester())
                .role(request.getRole() != null ? request.getRole() : "STUDENT")
                .build();

        User savedUser = userRepository.save(user);
        String token = tokenProvider.generateToken(savedUser.getEmail());

        return new AuthResponse(token, mapToUserDto(savedUser));
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + request.getEmail()));

        String token = tokenProvider.generateToken(user.getEmail());
        return new AuthResponse(token, mapToUserDto(user));
    }

    public UserDto getCurrentUserDto(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
        return mapToUserDto(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
    }

    public com.campushub.dto.UserProfileDto getUserProfile(String email) {
        User user = getUserByEmail(email);
        return com.campushub.dto.UserProfileDto.builder()
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .department(user.getDepartment())
                .semester(user.getSemester())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }

    @Transactional
    public com.campushub.dto.UserProfileDto updateUserProfile(String email, com.campushub.dto.UpdateProfileRequest request) {
        User user = getUserByEmail(email);
        if (request.getFullName() != null && !request.getFullName().isBlank()) {
            user.setFullName(request.getFullName());
        }
        if (request.getDepartment() != null) {
            user.setDepartment(request.getDepartment());
        }
        if (request.getSemester() != null) {
            user.setSemester(request.getSemester());
        }
        User updated = userRepository.save(user);
        return getUserProfile(updated.getEmail());
    }

    public UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .department(user.getDepartment())
                .semester(user.getSemester())
                .role(user.getRole())
                .build();
    }
}
