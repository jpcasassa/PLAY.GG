package com.playgg.forum.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.playgg.forum.client.UserClient;
import com.playgg.forum.dto.*;
import com.playgg.forum.exception.ResourceNotFoundException;
import com.playgg.forum.model.*;
import com.playgg.forum.repository.*;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

  @Mock private CommentRepository commentRepository;
  @Mock private PostRepository postRepository;
  @Mock private UserClient userClient;
  @InjectMocks private CommentService service;

  @Test
  void createShouldSaveCommentAndValidatePostAndUser() {
    when(postRepository.findById(1L)).thenReturn(Optional.of(post()));
    when(userClient.findById(10L)).thenReturn(ResponseEntity.ok().build());
    when(commentRepository.save(any(Comment.class)))
        .thenAnswer(
            invocation -> {
              Comment comment = invocation.getArgument(0);
              comment.setCommentId(1L);
              return comment;
            });

    CommentResponseDTO response = service.create(createDto());

    assertNotNull(response);
    assertEquals(1L, response.getCommentId());
    assertEquals(1L, response.getPostId());
    verify(postRepository).findById(1L);
    verify(userClient).findById(10L);
    verify(commentRepository).save(any(Comment.class));
  }

  @Test
  void createShouldThrowWhenPostDoesNotExist() {
    when(postRepository.findById(99L)).thenReturn(Optional.empty());
    CreateCommentDTO dto = createDto();
    dto.setPostId(99L);

    assertThrows(ResourceNotFoundException.class, () -> service.create(dto));
    verify(commentRepository, never()).save(any(Comment.class));
  }

  @Test
  void findByPostShouldReturnComments() {
    when(commentRepository.findByPostPostId(1L)).thenReturn(List.of(comment()));

    List<CommentResponseDTO> response = service.findByPost(1L);

    assertEquals(1, response.size());
    assertEquals("Buen aporte", response.get(0).getContent());
    verify(commentRepository).findByPostPostId(1L);
  }

  private CreateCommentDTO createDto() {
    CreateCommentDTO dto = new CreateCommentDTO();
    dto.setPostId(1L);
    dto.setUserId(10L);
    dto.setContent("Buen aporte");
    return dto;
  }

  private Post post() {
    return Post.builder().postId(1L).userId(10L).title("Post").content("Contenido").category("General").build();
  }

  private Comment comment() {
    return Comment.builder().commentId(1L).post(post()).userId(10L).content("Buen aporte").build();
  }
}
