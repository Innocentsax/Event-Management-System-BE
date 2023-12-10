package dev.Innocent.EventManagementSystem.service;

import dev.Innocent.EventManagementSystem.dto.AuthenticationResponse;
import dev.Innocent.EventManagementSystem.dto.LoginDto;
import dev.Innocent.EventManagementSystem.dto.UserDto;
import dev.Innocent.EventManagementSystem.dto.UserResponseDto;
import org.springframework.http.ResponseEntity;

    public interface AuthService {
        ResponseEntity<UserResponseDto> signUp(UserDto userDto);
        void logout();
        AuthenticationResponse loginUser(LoginDto loginDto);

        ResponseEntity<String> verifyEmail(String token);

        void initiatePasswordReset(String username);
    }


