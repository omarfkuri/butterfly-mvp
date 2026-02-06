package com.social.api.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
  name = "post_likes",
  uniqueConstraints = @UniqueConstraint(
    columnNames = {"user_id", "post_id"}
  ),
  indexes = {
    @Index(columnList = "post_id"),
    @Index(columnList = "user_id")
  }
)
public class PostLike
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id", nullable = false)
  private Post post;

  @Column(nullable = false)
  private LocalDateTime createdAt = LocalDateTime.now();
  
  @PrePersist
  protected void onCreate()
  {
    createdAt = LocalDateTime.now();
  }

  public Post getPost()
  {
    return post;
  }

  public User getUser()
  {
    return user;
  }

  public void setPost(Post post)
  {
    this.post = post;
  }

  public void setUser(User user)
  {
    this.user = user;
  }
}
