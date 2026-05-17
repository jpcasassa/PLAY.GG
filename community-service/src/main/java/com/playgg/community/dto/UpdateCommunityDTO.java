package com.playgg.community.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateCommunityDTO {
  @NotBlank private String name;
  @NotBlank private String description;
  private String bannerUrl;
  private Boolean active;
}
