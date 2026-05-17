package com.playgg.user.dto;

import com.playgg.user.model.Role;
import lombok.*;

/** DTO interno para auth-service. No se usa como respuesta publica porque contiene password. */
@Data
@Builder
public class UserAuthDTO {
  private Long userId;
  private String nickname;
  private String email;
  private String password;
  private Role role;
  private Boolean active;
}
