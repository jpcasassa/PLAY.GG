package com.playgg.auth.repository;

import com.playgg.auth.model.AuthSession;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthSessionRepository extends JpaRepository<AuthSession, Long> {
  Optional<AuthSession> findByRefreshTokenAndRevokedFalse(String refreshToken);

  Optional<AuthSession> findByTokenAndRevokedFalse(String token);
}
