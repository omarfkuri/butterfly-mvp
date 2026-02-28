package com.social.api.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.social.api.entity.User;

@Service
public class CustomUserDetailsService implements UserDetailsService
{
  private final UserService userService;

  public CustomUserDetailsService(UserService userService)
  {
    this.userService = userService;
  }

  @Override
  public UserDetails loadUserByUsername(String username)
  {
    User user = userService.findByUsername(username);

    return org.springframework.security.core.userdetails.User
        .withUsername(user.getUsername())
        .password(user.getPassword())
        .roles("USER")
        .build();
  }
}
