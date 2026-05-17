package com.playgg.search.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateSearchHistoryDTO {
  @NotNull private Long userId;
  @NotBlank private String query;
}
