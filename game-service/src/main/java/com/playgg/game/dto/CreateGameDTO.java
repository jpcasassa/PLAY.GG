package com.playgg.game.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/** DTO de entrada: evita acoplar la API a la entidad JPA. */
@Data
public class CreateGameDTO {
  @NotBlank private String title;
  @NotBlank private String genre;
  @NotBlank private String platform;
  private Boolean multiplayer;
  private Boolean competitive;
  private String imageUrl;
}
