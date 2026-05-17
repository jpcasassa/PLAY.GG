package com.playgg.notification.repository;

import com.playgg.notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repository: acceso a MySQL mediante Spring Data JPA. */
public interface NotificationRepository extends JpaRepository<Notification, Long> {}
