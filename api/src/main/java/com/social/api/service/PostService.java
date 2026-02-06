package com.social.api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.social.api.dto.PostDto;
import com.social.api.entity.Post;
import com.social.api.entity.User;
import com.social.api.events.PostEventPublisher;
import com.social.api.repository.PostRepository;
import com.social.api.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService
{
  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final PostEventPublisher eventPublisher;

  public PostService(
      PostRepository postRepository,
      UserRepository userRepository,
      PostEventPublisher eventPublisher)
  {
    this.postRepository = postRepository;
    this.userRepository = userRepository;
    this.eventPublisher = eventPublisher;
  }

  public List<PostDto> getAllPosts(String username)
  {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found"));

    return postRepository.findAllPostDtos(user);
  }

  public List<PostDto> getAllPostsByUsername(String username, String targetUsername)
  {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found"));

    return postRepository
        .findPostDtosByUsername(targetUsername, user);
  }

  public Optional<PostDto> getPostById(String username, Long id)
  {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found"));

    return postRepository.findPostDtoById(id, user);
  }

  public List<PostDto> getPostComments(String username, Long id)
  {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found"));

    Post parent = postRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Post not found"));

    return postRepository.findPostDtosByParent(parent, user);
  }

  @Transactional
  public Post createPost(String username, String title,
      String content)
  {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found"));

    Post post = new Post();
    post.setUser(user);
    post.setTitle(title);
    post.setContent(content);

    Post saved = postRepository.save(post);
    eventPublisher.postCreated(saved);
    return saved;
  }

  @Transactional
  public Post createComment(
      String username,
      String title,
      String content,
      Long parentId)
  {
    Post parent = postRepository.findById(parentId)
        .orElseThrow(
          () -> new RuntimeException("Parent does not exist"));

    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found"));

    Post comment = new Post();
    comment.setUser(user);
    comment.setTitle(title);
    comment.setContent(content);
    comment.setParent(parent);

    Post saved = postRepository.save(comment);
    eventPublisher.postCreated(saved);
    return saved;
  }

  @Transactional
  public Post updatePost(Long id, String title, String content)
  {
    Post post = postRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Post not found"));

    if (content != null)
    {
      post.setContent(content);
    }

    if (title != null)
    {
      post.setTitle(title);
    }

    Post saved = postRepository.save(post);
    eventPublisher.postUpdated(saved);
    return saved;
  }

  @Transactional
  public void deletePost(Long id)
  {
    Post post = postRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Post not found"));

    var desc = new ArrayList<PostInfo>();
    getAllDescendants(post, desc);

    postRepository.delete(post);

    eventPublisher.postDeleted(id, post.getUsername());
    
    for (var child : desc)
      eventPublisher.postDeleted(child.id(), child.username());
  }

  @Transactional(readOnly = true)
  public List<PostDto> getFeedForUser(String username)
  {
    User user = userRepository.findByUsername(username)
    .orElseThrow(() -> new RuntimeException("User not found"));

    return postRepository.findFeedPostDtos(user);
  }

  public boolean isPostOwner(Long postId, String username)
  {
    return postRepository.findById(postId)
        .map(post -> post.getUsername().equals(username))
        .orElse(false);
  }

  public long getPostCount(String username)
  {
    return postRepository.countByUsername(username);
  }

  private void getAllDescendants(Post current, List<PostInfo> desc)
  {
    for (var post : postRepository.findByParent(current))
    {
      desc.add(new PostInfo(post.getId(), post.getUsername()));

      getAllDescendants(post, desc);
    }
  }

  record PostInfo(Long id, String username) {};
}