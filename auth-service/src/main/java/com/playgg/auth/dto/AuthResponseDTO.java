package com.playgg.auth.dto;

import lombok.*;

@Data
@Builder
public class AuthResponseDTO {
  private Long userId;
  private String token;
  private String refreshToken;
  private String role;
}
