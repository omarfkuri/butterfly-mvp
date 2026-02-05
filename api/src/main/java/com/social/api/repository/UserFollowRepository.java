package com.social.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.social.api.entity.User;
import com.social.api.entity.UserFollow;
import java.util.List;
import java.util.Optional;

public interface UserFollowRepository extends JpaRepository<UserFollow, Long>
{
  Optional<UserFollow> findById(Long id);

  List<UserFollow> findAllByFollower(User follower);
  
  UserFollow findByFollowed(User followed);
  
  boolean existsByFollowerAndFollowed(User follower, User followed);

  long countByFollower(User follower);
  
  long countByFollowed(User followed);
}
