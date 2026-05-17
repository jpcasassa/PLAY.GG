package com.playgg.search.repository;

import com.playgg.search.model.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repository: acceso a MySQL mediante Spring Data JPA. */
public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {}
