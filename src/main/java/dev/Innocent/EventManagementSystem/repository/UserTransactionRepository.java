package dev.Innocent.EventManagementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import dev.Innocent.EventManagementSystem.model.UserTransactionInfo;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTransactionRepository extends JpaRepository<UserTransactionInfo, Long> {
    Optional<UserTransactionInfo> findByReference(String reference);
    Optional<UserTransactionInfo> findByEmail(String email);

}
