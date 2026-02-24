package com.social.api.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(unique = true, nullable = false)
  private String username;
  
  @Column(nullable = false)
  private String name;
  
  @Column(nullable = false)
  private String password;
  
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Post> posts = new ArrayList<>();

  @OneToOne(
    mappedBy = "user",
    cascade = CascadeType.ALL,
    orphanRemoval = true,
    fetch = FetchType.LAZY,
    optional = false
  )
  private UserProfile profile;

  @OneToMany(
    mappedBy = "user",
    cascade = CascadeType.ALL,
    orphanRemoval = true
  )
  private List<Image> images = new ArrayList<>();
  
  @Column(nullable = false)
  private LocalDateTime createdAt;

  
  @PrePersist
  protected void onCreate()
  {
    createdAt = LocalDateTime.now();
  }

  public LocalDateTime getCreatedAt()
  {
    return createdAt;
  }

  public Long getId()
  {
    return id;
  }
  
  public void setId(Long id)
  {
    this.id = id;
  }
  
  public String getUsername()
  {
    return username;
  }
  
  public void setUsername(String username)
  {
    this.username = username;
  }
  
  public String getPassword()
  {
    return password;
  }
  
  public void setPassword(String password)
  {
    this.password = password;
  }
  
  public List<Post> getPosts()
  {
    return posts;
  }
  
  public void setPosts(List<Post> posts)
  {
    this.posts = posts;
  }

  public UserProfile getProfile()
  {
    return profile;
  }

  public void setProfile(UserProfile profile)
  {
    this.profile = profile;
    profile.setUser(this);
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getName()
  {
    return name;
  }
}