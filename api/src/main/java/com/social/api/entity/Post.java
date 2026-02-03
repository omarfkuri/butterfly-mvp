package com.social.api.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "posts", indexes = {
  @Index(name = "idx_username", columnList = "username"),
  @Index(name = "idx_created_at", columnList = "createdAt")
})
public class Post
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  @JsonIgnore
  private User user;
  
  @Column(nullable = false)
  private String username;
  
  @Column(nullable = false, length = 128)
  private String title;
  
  @Column(nullable = false, length = 1024)
  private String content;
  
  @Column(nullable = false)
  private LocalDateTime createdAt;
  
  @PrePersist
  protected void onCreate()
  {
    createdAt = LocalDateTime.now();
    if (user != null && username == null)
    {
      username = user.getUsername();
    }
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
    if (user != null)
    {
      this.username = user.getUsername();
    }
  }
  
  public String getUsername()
  {
    return username;
  }
  
  public void setUsername(String username)
  {
    this.username = username;
  }
  
  public String getTitle()
  {
    return title;
  }
  
  public String getContent()
  {
    return content;
  }
  
  public void setContent(String content)
  {
    this.content = content;
  }
  
  public void setTitle(String title)
  {
    this.title = title;
  }
  
  public LocalDateTime getCreatedAt()
  {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt)
  {
    this.createdAt = createdAt;
  }
}