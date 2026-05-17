package com.playgg.chat.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/** DTO de entrada: evita acoplar la API a la entidad JPA. */
@Data
public class CreateMessageDTO {
  @NotNull private Long senderId;
  @NotNull private Long receiverId;

  @NotBlank
  @Size(max = 1000)
  private String content;

  private Boolean read;
}
