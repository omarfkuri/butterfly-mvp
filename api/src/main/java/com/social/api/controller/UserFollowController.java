package com.social.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.social.api.entity.User;
import com.social.api.service.UserFollowService;
import java.util.List;

@RestController
@RequestMapping("/follow")
public class UserFollowController
{
  private final UserFollowService userFollowService;

  public UserFollowController(
    UserFollowService userFollowService)
  {
    this.userFollowService = userFollowService;
  }

  @GetMapping("/get")
  public List<User> getFollowing(Authentication auth)
  {
    return userFollowService.getAllByFollower(auth.getName());
  }

  @PostMapping("/add/{followedID}")
  public ResponseEntity<String> follow(
    @PathVariable String followedID,
    Authentication auth)
  {
    userFollowService.followUser(auth.getName(), followedID);
    return ResponseEntity.ok("Followed");
  }

  @DeleteMapping("/rem/{followedID}")
  public ResponseEntity<String> unfollow(
    @PathVariable String followedID,
    Authentication auth)
  {
    userFollowService.unfollowUser(followedID);
    return ResponseEntity.ok("Unfollowed");
  }

  @GetMapping("/follows/{followedID}")
  public ResponseEntity<Boolean> follows(
    @PathVariable String followedID,
    Authentication auth)
  {
    var b = userFollowService.follows(auth.getName(), followedID);
    return ResponseEntity.ok(b);
  }

  @GetMapping("/follower-count/{username}")
  public ResponseEntity<Long> getFollowerCount(
    @PathVariable String username)
  {
    var b = userFollowService.getFollowerCount(username);
    return ResponseEntity.ok(b);
  }

  @GetMapping("/following-count/{username}")
  public ResponseEntity<Long> getFollowingCount(
    @PathVariable String username)
  {
    var b = userFollowService.getFollowingCount(username);
    return ResponseEntity.ok(b);
  }
}