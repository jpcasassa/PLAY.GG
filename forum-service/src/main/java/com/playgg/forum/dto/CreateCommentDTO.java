package com.playgg.forum.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateCommentDTO {
  @NotNull private Long postId;
  @NotNull private Long userId;
  @NotBlank private String content;
}
