package com.playgg.chat.dto;

import java.time.LocalDateTime;
import lombok.*;

/** DTO de respuesta. Nunca se retorna password u otros datos sensibles. */
@Data
@Builder
public class MessageResponseDTO {
  private Long messageId;
  private Long senderId;
  private Long receiverId;
  private String content;
  private LocalDateTime sentAt;
  private Boolean read;
}
