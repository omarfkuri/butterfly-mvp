package com.social.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.social.api.entity.Post;
import com.social.api.entity.PostLike;
import com.social.api.entity.User;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long>
{
  Optional<PostLike> findById(Long id);
  
  Optional<PostLike> findByUserAndPost(User user, Post post);
}
