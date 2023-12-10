package dev.Innocent.EventManagementSystem.repository;


import dev.Innocent.EventManagementSystem.model.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerification,Long> {
    EmailVerification findByToken(String token);
}
