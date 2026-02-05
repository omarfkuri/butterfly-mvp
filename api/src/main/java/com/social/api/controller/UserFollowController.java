package com.social.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.social.api.entity.User;
import com.social.api.ex.ResourceNotFoundException;
import com.social.api.service.UserFollowService;
import com.social.api.service.UserService;
import java.util.List;

@RestController
@RequestMapping("/follow")
public class UserFollowController
{
  private final UserService userService;
  private final UserFollowService userFollowService;

  public UserFollowController(
    UserService userService, 
    UserFollowService userFollowService)
  {
    this.userService = userService;
    this.userFollowService = userFollowService;
  }

  @GetMapping("/get")
  public List<User> getFollowing(Authentication auth)
  {
    var user = userService.findByUsername(auth.getName())
    .orElseThrow(() -> new ResourceNotFoundException(
      "Current user in get followers was not found"));

    return userFollowService.getAllByFollower(user);
  }

  @PostMapping("/add/{followedID}")
  public ResponseEntity<String> follow(
    @PathVariable String followedID,
    Authentication auth)
  {
    var follower = userService.findByUsername(auth.getName())
    .orElseThrow(() -> new ResourceNotFoundException("Follower not found: " + auth.getName()));

    var followed = userService.findByUsername(followedID)
    .orElseThrow(() -> new ResourceNotFoundException("Followed not found: " + followedID));

    if (follower.getId() == followed.getId())
      throw new RuntimeException(
        "Cannot follow yourself");

    userFollowService.followUser(follower, followed);
    return ResponseEntity.ok("Followed");
  }

  @DeleteMapping("/rem/{followedID}")
  public ResponseEntity<String> unfollow(
    @PathVariable String followedID,
    Authentication auth)
  {
    var follower = userService.findByUsername(auth.getName())
    .orElseThrow(() -> new ResourceNotFoundException("Follower not found: " + auth.getName()));

    var followed = userService.findByUsername(followedID)
    .orElseThrow(() -> new ResourceNotFoundException("Followed not found: " + followedID));

    if (follower.getId() == followed.getId())
      throw new RuntimeException(
        "Cannot unfollow yourself");

    userFollowService.unfollowUser(followed);
    return ResponseEntity.ok("Unfollowed");
  }

  @GetMapping("/follows/{followedID}")
  public ResponseEntity<Boolean> follows(
    @PathVariable String followedID,
    Authentication auth)
  {
    var follower = userService.findByUsername(auth.getName())
    .orElseThrow(() -> new ResourceNotFoundException("Follower not found: " + auth.getName()));

    var followed = userService.findByUsername(followedID)
    .orElseThrow(() -> new ResourceNotFoundException("Followed not found: " + followedID));

    var b = userFollowService.follows(follower, followed);
    return ResponseEntity.ok(b);
  }
}