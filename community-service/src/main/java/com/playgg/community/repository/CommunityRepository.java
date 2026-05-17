package com.playgg.community.repository;

import com.playgg.community.model.Community;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityRepository extends JpaRepository<Community, Long> {
  boolean existsByName(String name);
}
