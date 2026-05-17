package com.playgg.profile.repository;

import com.playgg.profile.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repository: acceso a MySQL mediante Spring Data JPA. */
public interface ProfileRepository extends JpaRepository<Profile, Long> {}
