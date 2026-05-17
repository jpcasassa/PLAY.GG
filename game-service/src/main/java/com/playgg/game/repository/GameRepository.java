package com.playgg.game.repository;

import com.playgg.game.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repository: acceso a MySQL mediante Spring Data JPA. */
public interface GameRepository extends JpaRepository<Game, Long> {}
