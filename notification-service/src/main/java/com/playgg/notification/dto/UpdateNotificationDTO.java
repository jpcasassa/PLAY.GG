package com.playgg.notification.dto;

import com.playgg.notification.model.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateNotificationDTO {
  @NotNull private Long userId;
  @NotBlank private String title;
  @NotBlank private String message;
  private NotificationType type;
  private Boolean read;
}
