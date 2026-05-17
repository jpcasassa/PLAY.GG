package com.playgg.community.repository;

import com.playgg.community.model.CommunityMember;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityMemberRepository extends JpaRepository<CommunityMember, Long> {
  List<CommunityMember> findByCommunityCommunityId(Long communityId);
}
