package com.playgg.forum.service;

import com.playgg.forum.dto.*;
import com.playgg.forum.exception.ResourceNotFoundException;
import com.playgg.forum.model.Post;
import com.playgg.forum.repository.PostRepository;
import com.playgg.forum.util.DateUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.*;
import org.springframework.stereotype.Service;

/** Service del foro: mantiene la logica de posts separada del controller. */
@Service
@RequiredArgsConstructor
public class PostService {
  private static final Logger logger = LoggerFactory.getLogger(PostService.class);
  private final PostRepository repository;

  public PostResponseDTO create(CreatePostDTO dto) {
    Post e = new Post();
    e.setUserId(dto.getUserId());
    e.setTitle(dto.getTitle());
    e.setContent(dto.getContent());
    e.setCategory(dto.getCategory());
    e.setLikes(0);
    e.setCreatedAt(DateUtil.now());
    logger.info("Creando post");
    return toResponse(repository.save(e));
  }

  public List<PostResponseDTO> findAll() {
    return repository.findAll().stream().map(this::toResponse).toList();
  }

  public PostResponseDTO findById(Long id) {
    return toResponse(get(id));
  }

  public PostResponseDTO update(Long id, UpdatePostDTO dto) {
    Post e = get(id);
    e.setTitle(dto.getTitle());
    e.setContent(dto.getContent());
    e.setCategory(dto.getCategory());
    e.setUpdatedAt(DateUtil.now());
    logger.info("Actualizando post {}", id);
    return toResponse(repository.save(e));
  }

  public void delete(Long id) {
    repository.delete(get(id));
    logger.info("Eliminando post {}", id);
  }

  private Post get(Long id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Post no encontrado con id: " + id));
  }

  private PostResponseDTO toResponse(Post e) {
    return PostResponseDTO.builder()
        .postId(e.getPostId())
        .userId(e.getUserId())
        .title(e.getTitle())
        .content(e.getContent())
        .category(e.getCategory())
        .likes(e.getLikes())
        .createdAt(e.getCreatedAt())
        .updatedAt(e.getUpdatedAt())
        .build();
  }
}
