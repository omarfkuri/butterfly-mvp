package com.social.api.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.social.api.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long>
{
  Optional<Image> findByKey(String key);
}