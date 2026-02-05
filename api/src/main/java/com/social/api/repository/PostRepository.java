package com.social.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.social.api.entity.Post;
import com.social.api.entity.User;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>
{

  Optional<Post> findById(Long id);

  List<Post> findByUsernameOrderByCreatedAtDesc(String username);

  List<Post> findAllByOrderByCreatedAtDesc();

  long countByUsername(String username);

  List<Post> findByParent(Post parent);

  boolean existsById(Long id);

  @Query("""
    SELECT p
    FROM Post p
    JOIN UserFollow f ON p.user = f.followed
    WHERE f.follower = :user
    ORDER BY p.createdAt DESC
  """)
  List<Post> findFeedForUser(@Param("user") User user);
}
