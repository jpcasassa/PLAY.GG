package com.playgg.auth.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequestDTO {
  @NotBlank private String nickname;
  @NotBlank private String firstName;
  @NotBlank private String lastName;
  @Email @NotBlank private String email;

  @Size(min = 8)
  private String password;

  @NotBlank private String country;
}
