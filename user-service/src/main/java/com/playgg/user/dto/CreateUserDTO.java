package com.playgg.user.dto;

import com.playgg.user.model.*;
import jakarta.validation.constraints.*;
import lombok.Data;

/** DTO de entrada: evita acoplar la API a la entidad JPA. */
@Data
public class CreateUserDTO {
  @NotBlank private String nickname;
  @NotBlank private String firstName;
  @NotBlank private String lastName;
  @Email @NotBlank private String email;

  @NotBlank
  @Size(min = 8)
  private String password;

  @NotBlank private String country;
  private Role role;
  private Boolean active;
}
