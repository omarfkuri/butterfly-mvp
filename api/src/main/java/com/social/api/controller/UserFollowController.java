package com.social.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.social.api.entity.User;
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
    var u = userService.findByUsername(auth.getName());

    if (u.isEmpty())
      throw new RuntimeException(
        "Current user in get followers was not found");

    return userFollowService.getAllByFollower(u.get());
  }

  @PostMapping("/add/{followedID}")
  public ResponseEntity<String> follow(
    @PathVariable Long followedID,
    Authentication auth)
  {
    var follower = userService.findByUsername(auth.getName());

    if (follower.isEmpty())
      throw new RuntimeException(
        "Current user in add follower was not found");

    var followed = userService.findById(followedID);

    if (followed.isEmpty())
      throw new RuntimeException(
        "Followed user in add follower was not found");

    userFollowService.followUser(follower.get(), followed.get());
    return ResponseEntity.ok("Followed");
  }

  @DeleteMapping("/rem/{followedID}")
  public ResponseEntity<String> unfollow(
    @PathVariable Long followedID,
    Authentication auth)
  {
    var followed = userService.findById(followedID);

    if (followed.isEmpty())
      throw new RuntimeException(
        "Followed user in add follower was not found");

    userFollowService.unfollowUser(followed.get());
    return ResponseEntity.ok("Unfollowed");
  }
}