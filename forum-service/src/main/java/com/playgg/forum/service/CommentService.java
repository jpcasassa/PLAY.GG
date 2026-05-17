package com.playgg.forum.service;

import com.playgg.forum.dto.*;
import com.playgg.forum.exception.ResourceNotFoundException;
import com.playgg.forum.model.*;
import com.playgg.forum.repository.*;
import com.playgg.forum.util.DateUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
  private static final Logger logger = LoggerFactory.getLogger(CommentService.class);
  private final CommentRepository commentRepository;
  private final PostRepository postRepository;

  public CommentResponseDTO create(CreateCommentDTO dto) {
    Post p =
        postRepository
            .findById(dto.getPostId())
            .orElseThrow(
                () ->
                    new ResourceNotFoundException("Post no encontrado con id: " + dto.getPostId()));
    Comment c = new Comment();
    c.setPost(p);
    c.setUserId(dto.getUserId());
    c.setContent(dto.getContent());
    c.setCreatedAt(DateUtil.now());
    logger.info("Creando comentario en post {}", dto.getPostId());
    return toResponse(commentRepository.save(c));
  }

  public List<CommentResponseDTO> findByPost(Long postId) {
    return commentRepository.findByPostPostId(postId).stream().map(this::toResponse).toList();
  }

  private CommentResponseDTO toResponse(Comment c) {
    return CommentResponseDTO.builder()
        .commentId(c.getCommentId())
        .postId(c.getPost().getPostId())
        .userId(c.getUserId())
        .content(c.getContent())
        .createdAt(c.getCreatedAt())
        .build();
  }
}
