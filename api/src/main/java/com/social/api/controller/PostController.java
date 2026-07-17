package com.social.api.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.social.api.dto.PostDto;
import com.social.api.entity.Post;
import com.social.api.service.PostService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@RestController
@RequestMapping("/posts")
public class PostController
{
    private final PostService postService;
    
    public PostController(
        PostService postService)
    {
        this.postService = postService;
    }
    
    @GetMapping("/all")
    public Page<PostDto> getAllPosts(
        @PageableDefault(size = 10, sort = "id") Pageable pageable,
        Authentication auth)
    {
        return postService.getAllPosts(auth.getName(), pageable);
    }
    
    @GetMapping("/following")
    public Page<PostDto> getAllFollowingPosts(
        @PageableDefault(size = 10, sort = "id") Pageable pageable,
        Authentication auth)
    {
        return postService.getFeedForUser(auth.getName(), pageable);
    }
    
    @GetMapping("/user/{username}")
    public Page<PostDto> getUserPublicPosts(
        @PageableDefault(size = 10, sort = "id") Pageable pageable,
        @PathVariable String username, Authentication auth)
    {
        return postService.getAllPostsByUsername(auth.getName(), username, pageable);
    }
    
    @GetMapping("/comments/all/{parentID}")
    public Page<PostDto> getComments(
        @PageableDefault(size = 10, sort = "id") Pageable pageable,
        @PathVariable Long parentID, Authentication auth)
    {
        return postService.getPostComments(auth.getName(), parentID, pageable);
    }
    
    @PostMapping("/comments/create/{parentID}")
    public ResponseEntity<Post> createComment(
        @PathVariable Long parentID,
        @Valid @RequestBody CreatePostRequest request,
        Authentication auth)
    {
        Post post = postService.createComment(
            auth.getName(),
            request.getContent(),
            parentID
        );
        return ResponseEntity.ok(post);
    }
    
    @GetMapping("/get/{id}")
    public PostDto getPublicPost(@PathVariable Long id, Authentication auth)
    {
        return postService.getPostById(auth.getName(), id);
    }
    
    @PostMapping("/create")
    public ResponseEntity<Post> createPost(
        @Valid @RequestBody CreatePostRequest request,
        Authentication auth)
    {
        Post post = postService.createPost(
            auth.getName(),
            request.getContent()
        );
        return ResponseEntity.ok(post);
    }
    
    @PutMapping("/update/{id}")
    public ResponseEntity<Post> updatePost(
        @PathVariable Long id,
        @Valid @RequestBody UpdatePostRequest request,
        Authentication auth)
    {
        if (!postService.isPostOwner(id, auth.getName()))
        {
            return ResponseEntity.status(403).build();
        }
        
        Post updated = postService.updatePost(
            id,
            request.getContent()
        );
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePost(
        @PathVariable Long id,
        Authentication auth)
    {
        if (!postService.isPostOwner(id, auth.getName()))
        {
            return ResponseEntity.status(403).build();
        }
        
        postService.deletePost(id);
        return ResponseEntity.ok().build();
    }
    
    public static class CreatePostRequest
    {
        @NotBlank
        @Size(min = 2, max = 512)
        @Pattern(
            regexp = "^[\\p{L}\\p{N}\\p{P}\\p{Zs}\\r\\n\\t]+$",
            message = "Body contains invalid characters"
        )
        private String content;
        
        public String getContent()
        {
            return content;
        }
        
        public void setContent(String content)
        {
            this.content = content;
        }
    }
    
    public static class UpdatePostRequest
    {
        @NotBlank
        @Size(min = 3, max = 512)
        @Pattern(
            regexp = "^[a-zA-Z0-9 _-]+$",
            message = "Body contains invalid characters"
        )
        private String content;
        
        public String getContent()
        {
            return content;
        }
        
        public void setContent(String content)
        {
            this.content = content;
        }
    }
}