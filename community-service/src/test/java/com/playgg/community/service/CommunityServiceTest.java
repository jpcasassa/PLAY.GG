package com.playgg.community.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.playgg.community.client.UserClient;
import com.playgg.community.dto.*;
import com.playgg.community.exception.ResourceNotFoundException;
import com.playgg.community.model.*;
import com.playgg.community.repository.*;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class CommunityServiceTest {

  @Mock private CommunityRepository repository;
  @Mock private CommunityMemberRepository memberRepository;
  @Mock private UserClient userClient;
  @InjectMocks private CommunityService service;

  @Test
  void createShouldSaveCommunityAndOwnerMember() {
    when(repository.existsByName("PLAY.GG")).thenReturn(false);
    when(userClient.findById(10L)).thenReturn(ResponseEntity.ok().build());
    when(repository.save(any(Community.class)))
        .thenAnswer(
            invocation -> {
              Community community = invocation.getArgument(0);
              community.setCommunityId(1L);
              return community;
            });
    when(repository.findById(1L)).thenReturn(Optional.of(community()));
    when(memberRepository.save(any(CommunityMember.class)))
        .thenAnswer(
            invocation -> {
              CommunityMember member = invocation.getArgument(0);
              member.setMemberId(100L);
              return member;
            });

    CommunityResponseDTO response = service.create(createDto());

    assertNotNull(response);
    assertEquals(1L, response.getCommunityId());
    assertEquals(Boolean.TRUE, response.getActive());
    verify(userClient, times(2)).findById(10L);
    verify(memberRepository).save(any(CommunityMember.class));
  }

  @Test
  void createShouldThrowWhenCommunityNameExists() {
    CreateCommunityDTO dto = createDto();
    when(repository.existsByName(dto.getName())).thenReturn(true);

    assertThrows(IllegalArgumentException.class, () -> service.create(dto));
    verify(repository, never()).save(any(Community.class));
  }

  @Test
  void findByIdShouldReturnCommunity() {
    when(repository.findById(1L)).thenReturn(Optional.of(community()));

    CommunityResponseDTO response = service.findById(1L);

    assertEquals("PLAY.GG", response.getName());
    verify(repository).findById(1L);
  }

  @Test
  void findAllShouldReturnCommunities() {
    when(repository.findAll()).thenReturn(List.of(community()));

    List<CommunityResponseDTO> response = service.findAll();

    assertEquals(1, response.size());
    assertEquals(10L, response.get(0).getOwnerId());
  }

  @Test
  void updateShouldModifyCommunity() {
    when(repository.findById(1L)).thenReturn(Optional.of(community()));
    when(repository.save(any(Community.class))).thenAnswer(invocation -> invocation.getArgument(0));

    CommunityResponseDTO response = service.update(1L, updateDto());

    assertEquals("PLAY.GG Pro", response.getName());
    assertEquals(Boolean.FALSE, response.getActive());
    verify(repository).save(any(Community.class));
  }

  @Test
  void deleteShouldRemoveCommunity() {
    Community community = community();
    when(repository.findById(1L)).thenReturn(Optional.of(community));

    service.delete(1L);

    verify(repository).delete(community);
  }

  @Test
  void addMemberShouldUseMemberRoleWhenRoleIsNull() {
    when(repository.findById(1L)).thenReturn(Optional.of(community()));
    when(userClient.findById(20L)).thenReturn(ResponseEntity.ok().build());
    when(memberRepository.save(any(CommunityMember.class)))
        .thenAnswer(
            invocation -> {
              CommunityMember member = invocation.getArgument(0);
              member.setMemberId(2L);
              return member;
            });
    AddMemberDTO dto = new AddMemberDTO();
    dto.setUserId(20L);

    CommunityMemberResponseDTO response = service.addMember(1L, dto);

    assertEquals(CommunityRole.MEMBER, response.getRole());
    assertEquals(20L, response.getUserId());
  }

  @Test
  void findByIdShouldThrowWhenCommunityDoesNotExist() {
    when(repository.findById(99L)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));
  }

  private CreateCommunityDTO createDto() {
    CreateCommunityDTO dto = new CreateCommunityDTO();
    dto.setOwnerId(10L);
    dto.setName("PLAY.GG");
    dto.setDescription("Comunidad gamer");
    dto.setBannerUrl("banner.png");
    return dto;
  }

  private UpdateCommunityDTO updateDto() {
    UpdateCommunityDTO dto = new UpdateCommunityDTO();
    dto.setName("PLAY.GG Pro");
    dto.setDescription("Comunidad competitiva");
    dto.setBannerUrl("banner-pro.png");
    dto.setActive(false);
    return dto;
  }

  private Community community() {
    return Community.builder()
        .communityId(1L)
        .ownerId(10L)
        .name("PLAY.GG")
        .description("Comunidad gamer")
        .bannerUrl("banner.png")
        .active(true)
        .build();
  }
}
