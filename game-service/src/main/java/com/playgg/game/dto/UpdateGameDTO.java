package com.playgg.game.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateGameDTO {
  @NotBlank private String title;
  @NotBlank private String genre;
  @NotBlank private String platform;
  private Boolean multiplayer;
  private Boolean competitive;
  private String imageUrl;
}
