package com.playgg.community.dto;

import com.playgg.community.model.CommunityRole;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AddMemberDTO {
  @NotNull private Long userId;
  private CommunityRole role;
}
