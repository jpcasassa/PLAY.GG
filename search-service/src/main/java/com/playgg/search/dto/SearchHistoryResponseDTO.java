package com.playgg.search.dto;

import java.time.LocalDateTime;
import lombok.*;

/** DTO de respuesta. Nunca se retorna password u otros datos sensibles. */
@Data
@Builder
public class SearchHistoryResponseDTO {
  private Long searchId;
  private Long userId;
  private String query;
  private LocalDateTime searchedAt;
}
