package dev.Innocent.EventManagementSystem.repository;

import dev.Innocent.EventManagementSystem.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query(value = "SELECT * FROM token WHERE users_id = ?1 AND (is_expired = 'false' or is_revoked = 'false')", nativeQuery = true)
    List<Token> findAllValidTokenByUser(Long id);

    Optional<Token> findByToken(String token);
}
