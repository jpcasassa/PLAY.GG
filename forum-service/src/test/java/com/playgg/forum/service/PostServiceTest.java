package com.playgg.forum.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.playgg.forum.client.UserClient;
import com.playgg.forum.dto.*;
import com.playgg.forum.exception.ResourceNotFoundException;
import com.playgg.forum.model.Post;
import com.playgg.forum.repository.PostRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

  @Mock private PostRepository repository;
  @Mock private UserClient userClient;
  @InjectMocks private PostService service;

  @Test
  void createShouldSavePostAndValidateUser() {
    when(userClient.findById(10L)).thenReturn(ResponseEntity.ok().build());
    when(repository.save(any(Post.class)))
        .thenAnswer(
            invocation -> {
              Post post = invocation.getArgument(0);
              post.setPostId(1L);
              return post;
            });

    PostResponseDTO response = service.create(createDto());

    assertNotNull(response);
    assertEquals(1L, response.getPostId());
    assertEquals(0, response.getLikes());
    verify(userClient).findById(10L);
    verify(repository).save(any(Post.class));
  }

  @Test
  void findByIdShouldReturnPost() {
    when(repository.findById(1L)).thenReturn(Optional.of(post()));

    PostResponseDTO response = service.findById(1L);

    assertEquals("Builds", response.getCategory());
    verify(repository).findById(1L);
  }

  @Test
  void findAllShouldReturnPosts() {
    when(repository.findAll()).thenReturn(List.of(post()));

    List<PostResponseDTO> response = service.findAll();

    assertEquals(1, response.size());
    assertEquals("Mi build", response.get(0).getTitle());
  }

  @Test
  void updateShouldModifyPost() {
    when(repository.findById(1L)).thenReturn(Optional.of(post()));
    when(repository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

    PostResponseDTO response = service.update(1L, updateDto());

    assertEquals("Nueva build", response.getTitle());
    assertEquals("Guias", response.getCategory());
    verify(repository).save(any(Post.class));
  }

  @Test
  void deleteShouldRemovePost() {
    Post post = post();
    when(repository.findById(1L)).thenReturn(Optional.of(post));

    service.delete(1L);

    verify(repository).delete(post);
  }

  @Test
  void findByIdShouldThrowWhenPostDoesNotExist() {
    when(repository.findById(99L)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));
  }

  private CreatePostDTO createDto() {
    CreatePostDTO dto = new CreatePostDTO();
    dto.setUserId(10L);
    dto.setTitle("Mi build");
    dto.setContent("Contenido del post");
    dto.setCategory("Builds");
    return dto;
  }

  private UpdatePostDTO updateDto() {
    UpdatePostDTO dto = new UpdatePostDTO();
    dto.setTitle("Nueva build");
    dto.setContent("Contenido actualizado");
    dto.setCategory("Guias");
    return dto;
  }

  private Post post() {
    return Post.builder()
        .postId(1L)
        .userId(10L)
        .title("Mi build")
        .content("Contenido del post")
        .category("Builds")
        .likes(0)
        .build();
  }
}
