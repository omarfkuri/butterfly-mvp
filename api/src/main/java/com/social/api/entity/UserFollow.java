package com.social.api.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
  name = "user_follows",
  uniqueConstraints = @UniqueConstraint(
    columnNames = {"follower_id", "followed_id"}
  ),
  indexes = {
    @Index(columnList = "follower_id"),
    @Index(columnList = "followed_id")
  }
)
public class UserFollow
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "follower_id", nullable = false)
  private User follower;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "followed_id", nullable = false)
  private User followed;

  @Column(nullable = false)
  private LocalDateTime createdAt = LocalDateTime.now();
  
  @PrePersist
  protected void onCreate()
  {
    createdAt = LocalDateTime.now();
  }

  public Long getId()
  {
    return id;
  }

  public User getFollowed()
  {
    return followed;
  }

  public User getFollower()
  {
    return follower;
  }

  public LocalDateTime getCreatedAt()
  {
    return createdAt;
  }

  public void setFollowed(User followed)
  {
    this.followed = followed;
  }

  public void setFollower(User follower)
  {
    this.follower = follower;
  }

  public void setCreatedAt(LocalDateTime createdAt)
  {
    this.createdAt = createdAt;
  }
}
