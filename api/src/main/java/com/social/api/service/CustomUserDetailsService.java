package com.social.api.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.social.api.entity.User;
import com.social.api.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService
{
  private final UserRepository repo;

  public CustomUserDetailsService(UserRepository repo)
  {
    this.repo = repo;
  }

  @Override
  public UserDetails loadUserByUsername(String username)
  {
    User user = repo.findByUsername(username)
        .orElseThrow(
          () -> new UsernameNotFoundException("User not found"));

    return org.springframework.security.core.userdetails.User
        .withUsername(user.getUsername())
        .password(user.getPassword())
        .roles("USER")
        .build();
  }
}
