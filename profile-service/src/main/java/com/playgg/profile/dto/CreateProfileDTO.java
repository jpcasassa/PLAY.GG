package com.playgg.profile.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/** DTO de entrada: evita acoplar la API a la entidad JPA. */
@Data
public class CreateProfileDTO {
  @NotNull private Long userId;
  private String avatarUrl;
  private String bannerUrl;

  @Size(max = 300)
  private String bio;

  private String steamUsername;
  private String discordUsername;
  private Long favoriteGameId;
  private String rank;
  private Integer level;
}
