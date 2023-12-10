package dev.Innocent.EventManagementSystem.serviceImpl;


import dev.Innocent.EventManagementSystem.dto.UserProfileDto;
import dev.Innocent.EventManagementSystem.model.User;
import dev.Innocent.EventManagementSystem.model.UserProfile;
import dev.Innocent.EventManagementSystem.repository.UserProfileRepository;
import dev.Innocent.EventManagementSystem.repository.UserRepository;
import dev.Innocent.EventManagementSystem.service.UserProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
@Service
@Slf4j
public class UserProfileServiceImp implements UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;

    public UserProfileServiceImp(UserProfileRepository userProfileRepository,
                                 UserRepository userRepository) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
    }
    @Override
    public UserProfileDto createUserProfile(UserProfileDto userProfileDto) {
        UserProfile userProfile = new UserProfile();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails =  (UserDetails) authentication.getPrincipal();
        User user =  userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(()->new NullPointerException("No such user"));
        userProfile.setUser(user);
        userProfile.setProfilePicture(userProfileDto.getProfilePicture());
        userProfile.setUserDescription(userProfileDto.getUserDescription());
        userProfile = userProfileRepository.save(userProfile);
        UserProfileDto createdUserProfile = new UserProfileDto();
        createdUserProfile.setUser(userProfile.getUser());
        createdUserProfile.setProfilePicture(userProfile.getProfilePicture());
        createdUserProfile.setUserDescription(userProfile.getUserDescription());
        return createdUserProfile;
    }


    @Override
    public UserProfileDto editUserProfile(UserProfileDto userProfileDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new NullPointerException("No such user"));

        UserProfile existingUserProfile = (UserProfile) userProfileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("User profile not found"));

        existingUserProfile.setProfilePicture(userProfileDto.getProfilePicture());
        existingUserProfile.setUserDescription(userProfileDto.getUserDescription());

        UserProfile updatedUserProfile = userProfileRepository.save(existingUserProfile);

        UserProfileDto editedUserProfile = new UserProfileDto();
        editedUserProfile.setUser(updatedUserProfile.getUser());
        editedUserProfile.setProfilePicture(updatedUserProfile.getProfilePicture());
        editedUserProfile.setUserDescription(updatedUserProfile.getUserDescription());

        return editedUserProfile;
    }

}