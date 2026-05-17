package com.playgg.chat.repository;

import com.playgg.chat.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repository: acceso a MySQL mediante Spring Data JPA. */
public interface MessageRepository extends JpaRepository<Message, Long> {}
