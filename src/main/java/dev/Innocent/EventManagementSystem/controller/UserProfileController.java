package dev.Innocent.EventManagementSystem.controller;


import dev.Innocent.EventManagementSystem.dto.UserProfileDto;
import dev.Innocent.EventManagementSystem.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/v1/user-profile")
@RequiredArgsConstructor
public class UserProfileController {
   private final UserProfileService userProfileService;
    @PostMapping("/create")
    public ResponseEntity<UserProfileDto> createUserProfile(@RequestBody UserProfileDto userProfileDto) {

        UserProfileDto createdUserProfile = userProfileService.createUserProfile(userProfileDto);

        if (createdUserProfile != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUserProfile);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PutMapping("/editProfile")
    public ResponseEntity<UserProfileDto> editUserProfile(@RequestBody UserProfileDto userProfileDto) {
        UserProfileDto editedUserProfile = userProfileService.editUserProfile(userProfileDto);
        return ResponseEntity.ok(editedUserProfile);
    }

}


