package com.playgg.community.dto;

import java.time.LocalDateTime;
import lombok.*;

@Data
@Builder
public class CommunityResponseDTO {
  private Long communityId;
  private Long ownerId;
  private String name;
  private String description;
  private String bannerUrl;
  private LocalDateTime createdAt;
  private Boolean active;
}
