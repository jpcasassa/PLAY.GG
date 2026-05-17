package com.playgg.collectible.dto;

import com.playgg.collectible.model.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateCollectibleDTO {
  @NotNull private Long userId;
  @NotNull private Long gameId;
  @NotBlank private String name;

  @Size(max = 300)
  private String description;

  private Rarity rarity;
}
