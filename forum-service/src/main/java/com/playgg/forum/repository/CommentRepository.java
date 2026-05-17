package com.playgg.forum.repository;

import com.playgg.forum.model.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
  List<Comment> findByPostPostId(Long postId);
}
