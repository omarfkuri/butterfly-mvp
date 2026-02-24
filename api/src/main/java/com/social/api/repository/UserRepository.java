package com.social.api.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.social.api.dto.UserDto;
import com.social.api.entity.User;

public interface UserRepository extends JpaRepository<User, Long>
{
  Optional<User> findByUsername(String username);
  
  boolean existsByUsername(String username);

  @Query("""
    SELECT new com.social.api.dto.UserDto(
      user.id,
      user.name,
      user.username,
      user.createdAt,
      profImg.key,
      coverImg.key
    )
    FROM User user
    LEFT JOIN UserProfile prof ON prof.user = user
    LEFT JOIN prof.profilePicture profImg
    LEFT JOIN prof.coverPicture coverImg
    WHERE user.username = :username
  """)
  Optional<UserDto> findDtoByUsername(
    @Param("username") String username
  );
}