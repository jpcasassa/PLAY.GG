package com.playgg.forum.dto;

import java.time.LocalDateTime;
import lombok.*;

@Data
@Builder
public class CommentResponseDTO {
  private Long commentId;
  private Long postId;
  private Long userId;
  private String content;
  private LocalDateTime createdAt;
}
