package dev.Innocent.EventManagementSystem.dto;

import com.decagon.eventbookingsq16.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserProfileDto {
    private User user;
    private String profilePicture;
    private String userDescription;


}
