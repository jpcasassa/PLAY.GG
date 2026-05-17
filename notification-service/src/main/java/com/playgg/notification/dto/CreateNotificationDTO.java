package com.playgg.notification.dto;

import com.playgg.notification.model.*;
import jakarta.validation.constraints.*;
import lombok.Data;

/** DTO de entrada: evita acoplar la API a la entidad JPA. */
@Data
public class CreateNotificationDTO {
  @NotNull private Long userId;
  @NotBlank private String title;
  @NotBlank private String message;
  private NotificationType type;
  private Boolean read;
}
