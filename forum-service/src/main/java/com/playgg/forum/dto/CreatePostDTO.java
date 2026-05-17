package com.playgg.forum.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreatePostDTO {
  @NotNull private Long userId;
  @NotBlank private String title;
  @NotBlank private String content;
  @NotBlank private String category;
}
