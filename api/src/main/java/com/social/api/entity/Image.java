package com.social.api.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "images")
public class Image
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "image_key", nullable = false, length = 1024)
  private String key;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  
  @PrePersist
  protected void onCreate()
  {
    createdAt = LocalDateTime.now();
  }
  
  public Long getId()
  {
    return id;
  }
  
  public void setId(Long id)
  {
    this.id = id;
  }
  
  public User getUser()
  {
    return user;
  }
  
  public void setUser(User user)
  {
    this.user = user;
  }
  
  public String getKey()
  {
    return key;
  }
  
  public void setKey(String key)
  {
    this.key = key;
  }
  
  public LocalDateTime getCreatedAt()
  {
    return createdAt;
  }
}