package com.playgg.forum.dto;

import java.time.LocalDateTime;
import lombok.*;

@Data
@Builder
public class PostResponseDTO {
  private Long postId;
  private Long userId;
  private String title;
  private String content;
  private String category;
  private Integer likes;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
