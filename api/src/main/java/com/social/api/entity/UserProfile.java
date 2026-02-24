package com.social.api.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_profile")
public class UserProfile
{
  @Id
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  @JoinColumn(name = "user_id")
  private User user;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @ManyToOne(fetch = FetchType.LAZY, optional = true)
  @JoinColumn(name = "profile_image_id", nullable = true)
  private Image profilePicture;

  @ManyToOne(fetch = FetchType.LAZY, optional = true)
  @JoinColumn(name = "cover_image_id", nullable = true)
  private Image coverPicture;

  @PrePersist
  protected void onCreate()
  {
    createdAt = LocalDateTime.now();
  }

  public Long getId()
  {
    return id;
  }

  public User getUser()
  {
    return user;
  }

  public LocalDateTime getCreatedAt()
  {
    return createdAt;
  }

  public Image getProfilePicture()
  {
    return profilePicture;
  }

  public Image getCoverPicture()
  {
    return coverPicture;
  }

  void setUser(User user)
  {
    this.user = user;
  }

  public void setProfilePicture(Image profilePicture)
  {
    this.profilePicture = profilePicture;
  }

  public void setCoverPicture(Image coverPicture)
  {
    this.coverPicture = coverPicture;
  }
}

