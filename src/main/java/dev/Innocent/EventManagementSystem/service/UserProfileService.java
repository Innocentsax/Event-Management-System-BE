package dev.Innocent.EventManagementSystem.service;

import dev.Innocent.EventManagementSystem.dto.UserProfileDto;
import org.springframework.stereotype.Service;

@Service
public interface UserProfileService {
    UserProfileDto createUserProfile(UserProfileDto userProfileDto);

    UserProfileDto editUserProfile(UserProfileDto userProfileDto);
}
