package com.playgg.user.repository;

import com.playgg.user.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repository: acceso a MySQL mediante Spring Data JPA. */
public interface UserRepository extends JpaRepository<User, Long> {
  boolean existsByNickname(String nickname);

  Optional<User> findByNickname(String nickname);

  boolean existsByEmail(String email);

  Optional<User> findByEmail(String email);
}
