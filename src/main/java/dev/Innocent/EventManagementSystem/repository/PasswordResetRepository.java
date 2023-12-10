package dev.Innocent.EventManagementSystem.repository;



import dev.Innocent.EventManagementSystem.model.PasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordReset, Long> {
    PasswordReset findByToken(String token);
}
