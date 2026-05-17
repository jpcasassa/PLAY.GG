package com.playgg.user.dto;

import com.playgg.user.model.*;
import java.time.LocalDateTime;
import lombok.*;

/** DTO de respuesta. Nunca se retorna password u otros datos sensibles. */
@Data
@Builder
public class UserResponseDTO {
  private Long userId;
  private String nickname;
  private String firstName;
  private String lastName;
  private String email;
  private String country;
  private Role role;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Boolean active;
}
