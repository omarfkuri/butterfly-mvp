package com.social.api.dto;

import java.time.LocalDateTime;

public record PostDto(
  Long id,
  String title,
  String content,
  String username,
  Long parentId,
  LocalDateTime createdAt,
  long likeCount,
  boolean likedByMe
) {}
