package com.social.api.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PostDto
{
    public final Long id;
    public final String content;
    public final UserDto author;
    public final Long parentId;
    public final LocalDateTime createdAt;
    public final long likeCount;
    public final boolean likedByMe;

    public List<PostImageDto> images = new ArrayList<>();

    public PostDto(
        Long id,
        String content,
        UserDto user,
        Long parentId,
        LocalDateTime createdAt,
        long likeCount,
        boolean likedByMe
    )
    {
        this.id = id;
        this.content = content;
        this.author = user;
        this.parentId = parentId;
        this.createdAt = createdAt;
        this.likeCount = likeCount;
        this.likedByMe = likedByMe;
    }

    public Long getId()
    {
      return id;
    }

    public String getContent()
    {
        return content;
    }

    public UserDto getAuthor()
    {
        return author;
    }

    public Long getParentId()
    {
        return parentId;
    }

    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }

    public long getLikeCount()
    {
        return likeCount;
    }

    public boolean getLikedByMe()
    {
        return likedByMe;
    }
  }
