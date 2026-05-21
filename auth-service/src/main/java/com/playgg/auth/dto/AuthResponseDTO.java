package com.playgg.auth.dto;

import lombok.*;

@Data
@Builder
public class AuthResponseDTO {
  private Long userId;
  private String nickname;
  private String email;
  private String role;
  private String message;
}
