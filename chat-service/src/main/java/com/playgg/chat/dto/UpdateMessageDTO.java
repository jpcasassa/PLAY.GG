package com.playgg.chat.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateMessageDTO {
  @NotNull private Long senderId;
  @NotNull private Long receiverId;

  @NotBlank
  @Size(max = 1000)
  private String content;

  private Boolean read;
}
