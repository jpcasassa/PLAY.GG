package com.playgg.community.dto;

import com.playgg.community.model.CommunityRole;
import java.time.LocalDateTime;
import lombok.*;

@Data
@Builder
public class CommunityMemberResponseDTO {
  private Long memberId;
  private Long communityId;
  private Long userId;
  private LocalDateTime joinedAt;
  private CommunityRole role;
}
