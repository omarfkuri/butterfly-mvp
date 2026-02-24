package com.social.api.dto;

import java.time.LocalDateTime;

public record UserDto(
  Long id,
  String name,
  String username,
  LocalDateTime createdAt,
  String profilePictureURL
) {}
