package com.playgg.community.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateCommunityDTO {
  @NotNull private Long ownerId;
  @NotBlank private String name;
  @NotBlank private String description;
  private String bannerUrl;
}
