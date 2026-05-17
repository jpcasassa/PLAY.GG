package com.playgg.collectible.dto;

import com.playgg.collectible.model.*;
import java.time.LocalDateTime;
import lombok.*;

/** DTO de respuesta. Nunca se retorna password u otros datos sensibles. */
@Data
@Builder
public class CollectibleResponseDTO {
  private Long collectibleId;
  private Long userId;
  private Long gameId;
  private String name;
  private String description;
  private Rarity rarity;
  private LocalDateTime unlockedAt;
}
