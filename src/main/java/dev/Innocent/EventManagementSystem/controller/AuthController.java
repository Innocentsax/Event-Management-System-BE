package dev.Innocent.EventManagementSystem.controller;

import dev.Innocent.EventManagementSystem.dto.*;
import dev.Innocent.EventManagementSystem.exception.*;
import dev.Innocent.EventManagementSystem.service.AuthService;
import dev.Innocent.EventManagementSystem.serviceImpl.AuthServiceImpl;
import dev.Innocent.EventManagementSystem.serviceImpl.UserServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService userService;
    private final AuthServiceImpl auth;
    private final UserServiceImp userServiceImp;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signUp(@RequestBody UserDto userDto) {
         return userService.signUp(userDto);
    }
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> loginUser(@RequestBody LoginDto loginDto) {
        AuthenticationResponse response = userService.loginUser(loginDto);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/logout")
    public void logout() {
        userService.logout();
    }
  
    @GetMapping("/register/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        return userService.verifyEmail(token);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> initiatePasswordReset(@RequestParam("username") String username) {
        try {
            auth.initiatePasswordReset(username);
            return ResponseEntity.ok("Password reset process initiated. Check your inbox for instructions.");
        } catch (TokenExpiredException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reset token has expired or doesn't exist.", e);
        }
    }

    @GetMapping("/verify-password-token/{token}")
    public ResponseEntity<String> verifyPasswordToken(@PathVariable String token) {
        auth.verifyPasswordToken(token);
        return ResponseEntity.ok("Token successfully verified");
    }
    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestBody PasswordRequestDto passwordRequestDto) throws ResourceNotFoundException {
        return userServiceImp.resetPassword(token, passwordRequestDto);
    }


    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody PasswordRequestDto passwordRequestDto) {
        try {
            userServiceImp.updatePassword(passwordRequestDto);
            return ResponseEntity.ok("Password reset successful");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (InvalidPasswordException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid old password");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to reset password");
        }
    }

}

