package com.playgg.profile.dto;

import java.time.LocalDateTime;
import lombok.*;

/** DTO de respuesta. Nunca se retorna password u otros datos sensibles. */
@Data
@Builder
public class ProfileResponseDTO {
  private Long profileId;
  private Long userId;
  private String avatarUrl;
  private String bannerUrl;
  private String bio;
  private String steamUsername;
  private String discordUsername;
  private Long favoriteGameId;
  private String rank;
  private Integer level;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
