package com.playgg.user.dto;

import com.playgg.user.model.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateUserDTO {
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
