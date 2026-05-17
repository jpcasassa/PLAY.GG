package com.playgg.collectible.repository;

import com.playgg.collectible.model.Collectible;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repository: acceso a MySQL mediante Spring Data JPA. */
public interface CollectibleRepository extends JpaRepository<Collectible, Long> {}
