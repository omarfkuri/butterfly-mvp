package com.social.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.social.api.dto.UserProfileDto;
import com.social.api.service.UserProfileService;

@RestController
@RequestMapping("/profile")
public class UserProfileController
{
  private final UserProfileService userProfileService;

  public UserProfileController(
    UserProfileService userProfileService)
  {
    this.userProfileService = userProfileService;
  }

  @GetMapping("/get/{username}")
  public UserProfileDto getFollowing(
    @PathVariable String username)
  {
    return userProfileService.getUserProfile(username);
  }

  @PostMapping("/set/profile-picture")
  public ResponseEntity<?> setProfilePicture(
    @RequestParam("file") MultipartFile file,
    Authentication auth
  )
  {
    userProfileService.setProfilePicture(auth.getName(), file);

    return ResponseEntity.ok().build();
  }

  @PostMapping("/set/cover-picture")
  public ResponseEntity<?> setCoverPicture(
    @RequestParam("file") MultipartFile file,
    Authentication auth
  )
  {
    userProfileService.setCoverPicture(auth.getName(), file);
    
    return ResponseEntity.ok().build();
  }
}