package com.social.api.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.social.api.dto.UserDto;
import com.social.api.ex.ResourceNotFoundException;
import com.social.api.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController
{
  private UserService userService;

  UserController(UserService userService)
  {
    this.userService = userService;
  }

  @GetMapping("/me")
  public UserDto me(Authentication auth)
  {
    return userService.findDtoByUsername(auth.getName())
    .orElseThrow(() -> new ResourceNotFoundException(
      "Current user in get followers was not found"));
  }

  @GetMapping("/get/{username}")
  public UserDto getUser(
    @PathVariable String username
  )
  {
    return userService.findDtoByUsername(username)
    .orElseThrow(() -> new ResourceNotFoundException(
      "Current user in get followers was not found"));
  }
}
