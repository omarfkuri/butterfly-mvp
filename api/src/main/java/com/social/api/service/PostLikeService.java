package com.social.api.service;

import org.springframework.stereotype.Service;

import com.social.api.entity.PostLike;
import com.social.api.repository.PostLikeRepository;
import com.social.api.repository.PostRepository;
import com.social.api.repository.UserRepository;

@Service
public class PostLikeService
{
  private final UserRepository userRepository;
  private final PostRepository postRepository;
  private final PostLikeRepository postLikeRepository;

  public PostLikeService(
      UserRepository userRepository,
      PostRepository postRepository,
      PostLikeRepository postLikeRepository)
  {
    this.userRepository = userRepository;
    this.postRepository = postRepository;
    this.postLikeRepository = postLikeRepository;
  }

  public PostLike likePost(String username, Long postID)
  {
    var user = userRepository.findByUsername(username)
    .orElseThrow(() -> new IllegalArgumentException("User not found"));

    var post = postRepository.findById(postID)
    .orElseThrow(() -> new IllegalArgumentException("Post not found"));

    if (!postLikeRepository.findByUserAndPost(user, post).isEmpty())
      throw new IllegalArgumentException("Post already liked");

    var like = new PostLike();
    like.setPost(post);
    like.setUser(user);

    postLikeRepository.save(like);
    return like;
  }

  public PostLike dislikePost(String username, Long postID)
  {
    var user = userRepository.findByUsername(username)
    .orElseThrow(() -> new IllegalArgumentException("User not found"));

    var post = postRepository.findById(postID)
    .orElseThrow(() -> new IllegalArgumentException("Post not found"));

    var like = postLikeRepository.findByUserAndPost(user, post)
    .orElseThrow(() -> new IllegalArgumentException("Post not liked"));

    postLikeRepository.delete(like);
    return like;
  }
}