package com.social.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.social.api.service.PostLikeService;

@RestController
@RequestMapping("/like")
public class PostLikeController
{
  private final PostLikeService likeService;

  public PostLikeController(
    PostLikeService likeService)
  {
    this.likeService = likeService;
  }

  @PostMapping("/add/{postID}")
  public ResponseEntity<String> follow(
    @PathVariable Long postID,
    Authentication auth)
  {
    likeService.likePost(auth.getName(), postID);
    return ResponseEntity.ok("Liked");
  }

  @DeleteMapping("/rem/{postID}")
  public ResponseEntity<String> unfollow(
    @PathVariable Long postID,
    Authentication auth)
  {
    likeService.dislikePost(auth.getName(), postID);
    return ResponseEntity.ok("Disliked");
  }
}