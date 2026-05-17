package com.playgg.game.dto;

import lombok.*;

/** DTO de respuesta. Nunca se retorna password u otros datos sensibles. */
@Data
@Builder
public class GameResponseDTO {
  private Long gameId;
  private String title;
  private String genre;
  private String platform;
  private Boolean multiplayer;
  private Boolean competitive;
  private String imageUrl;
}
