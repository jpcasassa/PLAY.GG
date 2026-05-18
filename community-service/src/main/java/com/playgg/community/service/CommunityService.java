package com.playgg.community.service;

import com.playgg.community.client.UserClient;
import com.playgg.community.dto.*;
import com.playgg.community.exception.ResourceNotFoundException;
import com.playgg.community.model.*;
import com.playgg.community.repository.*;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommunityService {
  private static final Logger logger = LoggerFactory.getLogger(CommunityService.class);
  private final CommunityRepository repository;
  private final CommunityMemberRepository memberRepository;
  private final UserClient userClient;

  public CommunityResponseDTO create(CreateCommunityDTO dto) {
    if (repository.existsByName(dto.getName()))
      throw new IllegalArgumentException("Nombre de comunidad ya existe");
    userClient.findById(dto.getOwnerId());
    Community c = new Community();
    c.setOwnerId(dto.getOwnerId());
    c.setName(dto.getName());
    c.setDescription(dto.getDescription());
    c.setBannerUrl(dto.getBannerUrl());
    c.setCreatedAt(LocalDateTime.now());
    c.setActive(true);
    Community saved = repository.save(c);
    addMember(saved.getCommunityId(), ownerDto(dto.getOwnerId()));
    logger.info("Creando comunidad {}", dto.getName());
    return toResponse(saved);
  }

  private AddMemberDTO ownerDto(Long id) {
    AddMemberDTO dto = new AddMemberDTO();
    dto.setUserId(id);
    dto.setRole(CommunityRole.OWNER);
    return dto;
  }

  public List<CommunityResponseDTO> findAll() {
    return repository.findAll().stream().map(this::toResponse).toList();
  }

  public CommunityResponseDTO findById(Long id) {
    return toResponse(get(id));
  }

  public CommunityResponseDTO update(Long id, UpdateCommunityDTO dto) {
    Community c = get(id);
    c.setName(dto.getName());
    c.setDescription(dto.getDescription());
    c.setBannerUrl(dto.getBannerUrl());
    c.setActive(dto.getActive());
    return toResponse(repository.save(c));
  }

  public void delete(Long id) {
    repository.delete(get(id));
    logger.info("Eliminando comunidad {}", id);
  }

  public CommunityMemberResponseDTO addMember(Long id, AddMemberDTO dto) {
    Community c = get(id);
    userClient.findById(dto.getUserId());
    CommunityMember m = new CommunityMember();
    m.setCommunity(c);
    m.setUserId(dto.getUserId());
    m.setRole(dto.getRole() == null ? CommunityRole.MEMBER : dto.getRole());
    m.setJoinedAt(LocalDateTime.now());
    return toMember(memberRepository.save(m));
  }

  public List<CommunityMemberResponseDTO> members(Long id) {
    return memberRepository.findByCommunityCommunityId(id).stream().map(this::toMember).toList();
  }

  private Community get(Long id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Comunidad no encontrada con id: " + id));
  }

  private CommunityResponseDTO toResponse(Community c) {
    return CommunityResponseDTO.builder()
        .communityId(c.getCommunityId())
        .ownerId(c.getOwnerId())
        .name(c.getName())
        .description(c.getDescription())
        .bannerUrl(c.getBannerUrl())
        .createdAt(c.getCreatedAt())
        .active(c.getActive())
        .build();
  }

  private CommunityMemberResponseDTO toMember(CommunityMember m) {
    return CommunityMemberResponseDTO.builder()
        .memberId(m.getMemberId())
        .communityId(m.getCommunity().getCommunityId())
        .userId(m.getUserId())
        .joinedAt(m.getJoinedAt())
        .role(m.getRole())
        .build();
  }
}
