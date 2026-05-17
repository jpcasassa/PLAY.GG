package com.playgg.search.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/** DTO de entrada: evita acoplar la API a la entidad JPA. */
@Data
public class CreateSearchHistoryDTO {
  @NotNull private Long userId;
  @NotBlank private String query;
}
