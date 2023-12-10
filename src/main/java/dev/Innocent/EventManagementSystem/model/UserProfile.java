package dev.Innocent.EventManagementSystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class UserProfile extends BaseEntity {
    @OneToOne
    @JsonIgnore
    private User user;
    private String profilePicture;
    private String userDescription;
}
