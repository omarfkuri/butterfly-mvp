package com.social.api.repository;

import com.social.api.dto.PostDto;
import com.social.api.entity.Post;
import com.social.api.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>
{
  Optional<Post> findById(Long id);

  long countByUsername(String username);

  boolean existsById(Long id);

  List<Post> findByParent(Post parent);

  @Query("""
    SELECT new com.social.api.dto.PostDto(
      p.id,
      p.title,
      p.content,
      p.username,
      p.parent.id,
      p.createdAt,
      COUNT(pl),
      SUM(CASE WHEN pl.user = :viewer THEN 1 ELSE 0 END) > 0
    )
    FROM Post p
    LEFT JOIN PostLike pl ON pl.post = p
    GROUP BY p
    ORDER BY p.createdAt DESC
  """)
  Page<PostDto> findAllPostDtos(@Param("viewer") User viewer, Pageable pageable);

  @Query("""
    SELECT new com.social.api.dto.PostDto(
      p.id,
      p.title,
      p.content,
      p.username,
      p.parent.id,
      p.createdAt,
      COUNT(pl),
      SUM(CASE WHEN pl.user = :viewer THEN 1 ELSE 0 END) > 0
    )
    FROM Post p
    LEFT JOIN PostLike pl ON pl.post = p
    WHERE p.username = :username
    GROUP BY p
    ORDER BY p.createdAt DESC
  """)
  Page<PostDto> findPostDtosByUsername(
    @Param("username") String username,
    @Param("viewer") User viewer, 
    Pageable pageable
  );

  @Query("""
    SELECT new com.social.api.dto.PostDto(
      p.id,
      p.title,
      p.content,
      p.username,
      p.parent.id,
      p.createdAt,
      COUNT(pl),
      SUM(CASE WHEN pl.user = :viewer THEN 1 ELSE 0 END) > 0
    )
    FROM Post p
    LEFT JOIN PostLike pl ON pl.post = p
    WHERE p.parent = :parent
    GROUP BY p
    ORDER BY p.createdAt ASC
  """)
  Page<PostDto> findPostDtosByParent(
    @Param("parent") Post parent,
    @Param("viewer") User viewer, 
    Pageable pageable
  );

  @Query("""
    SELECT new com.social.api.dto.PostDto(
      p.id,
      p.title,
      p.content,
      p.username,
      p.parent.id,
      p.createdAt,
      COUNT(pl),
      SUM(CASE WHEN pl.user = :viewer THEN 1 ELSE 0 END) > 0
    )
    FROM Post p
    JOIN UserFollow f ON p.user = f.followed
    LEFT JOIN PostLike pl ON pl.post = p
    WHERE f.follower = :viewer
    GROUP BY p
    ORDER BY p.createdAt DESC
  """)
  Page<PostDto> findFeedPostDtos(@Param("viewer") User viewer, 
    Pageable pageable);

  @Query("""
    SELECT new com.social.api.dto.PostDto(
      p.id,
      p.title,
      p.content,
      p.username,
      p.parent.id,
      p.createdAt,
      COUNT(pl),
      SUM(CASE WHEN pl.user = :viewer THEN 1 ELSE 0 END) > 0
    )
    FROM Post p
    LEFT JOIN PostLike pl ON pl.post = p
    WHERE p.id = :id
    GROUP BY p
  """)
  Optional<PostDto> findPostDtoById(
    @Param("id") Long id,
    @Param("viewer") User viewer
  );
}
