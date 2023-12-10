package dev.Innocent.EventManagementSystem.repository;

import dev.Innocent.EventManagementSystem.model.User;
import dev.Innocent.EventManagementSystem.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    Optional<Object> findByUser(User user);
}
