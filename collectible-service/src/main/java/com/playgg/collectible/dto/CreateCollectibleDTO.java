package com.playgg.collectible.dto;

import com.playgg.collectible.model.*;
import jakarta.validation.constraints.*;
import lombok.Data;

/** DTO de entrada: evita acoplar la API a la entidad JPA. */
@Data
public class CreateCollectibleDTO {
  @NotNull private Long userId;
  @NotNull private Long gameId;
  @NotBlank private String name;

  @Size(max = 300)
  private String description;

  private Rarity rarity;
}
