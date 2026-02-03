package com.social.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.social.api.entity.Post;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>
{
  Optional<Post> findById(Long id);
  
  List<Post> findByUsernameOrderByCreatedAtDesc(String username);
  
  List<Post> findAllByOrderByCreatedAtDesc();
  
  long countByUsername(String username);
}