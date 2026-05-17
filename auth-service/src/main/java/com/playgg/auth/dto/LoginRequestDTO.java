package com.playgg.auth.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class LoginRequestDTO {
  @Email @NotBlank private String email;
  @NotBlank private String password;
}
