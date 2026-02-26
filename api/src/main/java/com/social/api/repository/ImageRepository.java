package com.social.api.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.social.api.dto.ImageDto;
import com.social.api.entity.Image;
import com.social.api.entity.User;

public interface ImageRepository extends JpaRepository<Image, Long>
{
    Optional<Image> findByKey(String key);

    @Query("""
        SELECT new com.social.api.dto.ImageDto(i.key)
        FROM Image i
        WHERE i.user = :user
    """)
    Page<ImageDto> findAllByUser(@Param("user") User user, Pageable pageable);
}