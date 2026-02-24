package com.social.api.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.social.api.dto.PostDto;
import com.social.api.dto.PostImageDto;
import com.social.api.dto.UserDto;
import com.social.api.entity.Post;
import com.social.api.entity.User;
import com.social.api.events.PostEventPublisher;
import com.social.api.repository.PostRepository;
import com.social.api.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService
{
  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final PostEventPublisher eventPublisher;
  private final FirebaseStorageService storageService;

  public PostService(
      FirebaseStorageService storageService,
      PostRepository postRepository,
      UserRepository userRepository,
      PostEventPublisher eventPublisher)
  {
    this.storageService = storageService;
    this.postRepository = postRepository;
    this.userRepository = userRepository;
    this.eventPublisher = eventPublisher;
  }

  public Page<PostDto> getAllPosts(String username, Pageable pageable)
  {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found"));


    return resolvePostPage(postRepository.findAllPostDtos(user, pageable));
  }

  @Transactional(readOnly = true)
  public Page<PostDto> getFeedForUser(String username, 
    Pageable pageable)
  {
    User user = userRepository.findByUsername(username)
    .orElseThrow(() -> new RuntimeException("User not found"));

    var page = postRepository.findFeedPostDtos(user, pageable);

    List<Long> postIds = page.getContent()
    .stream()
    .map(PostDto::getId)
    .toList();

    Map<Long, List<PostImageDto>> imagesByPost =
    postRepository.findPostImages(postIds)
    .stream()
    .collect(Collectors.groupingBy(PostImageDto::postId));

    page.getContent().forEach(post ->
      post.images = 
        imagesByPost.getOrDefault(post.id, List.of())
    );

    return resolvePostPage(page);
  }

  public Page<PostDto> getAllPostsByUsername(
    String username, 
    String targetUsername, 
    Pageable pageable
  )
  {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found"));

    return resolvePostPage(postRepository
        .findPostDtosByUsername(targetUsername, user, pageable));
  }

  public Page<PostDto> getPostComments(
    String username, 
    Long id, 
    Pageable pageable
  )
  {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found"));

    Post parent = postRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Post not found"));

    return resolvePostPage(postRepository.findPostDtosByParent(parent, user, pageable));
  }

  public PostDto getPostById(String username, Long id)
  {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found"));

    var post = postRepository.findPostDtoById(id, user)
        .orElseThrow(() -> new RuntimeException("Post not found"));

    return resolvePost(post);
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

  private Page<PostDto> resolvePostPage(Page<PostDto> page)
  {
    List<PostDto> resolved = page.getContent()
    .stream()
    .map(this::resolvePost)
    .toList();
    
    return new PageImpl<>(resolved, page.getPageable(), page.getTotalElements());
  }

  private PostDto resolvePost(PostDto post)
  {
    var author = post.getAuthor();

    return new PostDto(
      post.getId(),
      post.getTitle(),
      post.getContent(),
      new UserDto(
        author.id(),
        author.name(),
        author.username(),
        author.createdAt(),
        author.profilePictureURL() == null
        ? null
        : storageService.getPublicUrl(author.profilePictureURL()),
        author.coverPictureURL() == null
        ? null
        : storageService.getPublicUrl(author.coverPictureURL())
      ),
      post.getParentId(),
      post.getCreatedAt(),
      post.getLikeCount(),
      post.getLikedByMe()
    );
  }

  record PostInfo(Long id, String username) {};
}