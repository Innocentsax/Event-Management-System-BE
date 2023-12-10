package dev.Innocent.EventManagementSystem.service;


import dev.Innocent.EventManagementSystem.dto.PasswordRequestDto;
import dev.Innocent.EventManagementSystem.exception.ResourceNotFoundException;
import dev.Innocent.EventManagementSystem.exception.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    void updatePassword(PasswordRequestDto passwordRequestDto) throws UserNotFoundException;
    ResponseEntity<String> resetPassword(String token, String newPassword, String confirmPassword) throws ResourceNotFoundException;

}
