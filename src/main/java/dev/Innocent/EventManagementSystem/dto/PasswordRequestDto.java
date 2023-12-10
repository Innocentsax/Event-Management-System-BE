package dev.Innocent.EventManagementSystem.dto;


import lombok.Data;

    @Data
    public class PasswordRequestDto {
        private String username;
        private String oldPassword;
        private String newPassword;
        private String confirmPassword;
    }
