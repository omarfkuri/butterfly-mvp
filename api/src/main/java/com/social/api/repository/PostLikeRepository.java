package com.social.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.social.api.entity.PostLike;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long>
{
  Optional<PostLike> findById(Long id);
}
