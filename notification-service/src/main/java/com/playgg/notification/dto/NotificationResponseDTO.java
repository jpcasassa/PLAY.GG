package com.playgg.notification.dto;

import com.playgg.notification.model.*;
import java.time.LocalDateTime;
import lombok.*;

/** DTO de respuesta. Nunca se retorna password u otros datos sensibles. */
@Data
@Builder
public class NotificationResponseDTO {
  private Long notificationId;
  private Long userId;
  private String title;
  private String message;
  private NotificationType type;
  private Boolean read;
  private LocalDateTime createdAt;
}
