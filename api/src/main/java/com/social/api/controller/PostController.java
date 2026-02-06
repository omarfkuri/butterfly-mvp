package com.social.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.social.api.dto.PostDto;
import com.social.api.entity.Post;
import com.social.api.service.PostService;
import com.social.api.service.UserFollowService;

import java.util.List;

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
    public List<PostDto> getAllPosts(Authentication auth)
    {
        return postService.getAllPosts(auth.getName());
    }
    
    @GetMapping("/following")
    public List<PostDto> getAllFollowingPosts(Authentication auth)
    {
        return postService.getFeedForUser(auth.getName());
    }
    
    @GetMapping("/user/{username}")
    public List<PostDto> getUserPublicPosts(@PathVariable String username, Authentication auth)
    {
        return postService.getAllPostsByUsername(auth.getName(), username);
    }
    
    @GetMapping("/comments/all/{parentID}")
    public List<PostDto> getComments(@PathVariable Long parentID, Authentication auth)
    {
        return postService.getPostComments(auth.getName(), parentID);
    }
    
    @PostMapping("/comments/create/{parentID}")
    public ResponseEntity<Post> createComment(
        @PathVariable Long parentID,
        @RequestBody CreatePostRequest request,
        Authentication auth)
    {
        Post post = postService.createComment(
            auth.getName(),
            request.getTitle(),
            request.getContent(),
            parentID
        );
        return ResponseEntity.ok(post);
    }
    
    @GetMapping("/get/{id}")
    public ResponseEntity<PostDto> getPublicPost(@PathVariable Long id, Authentication auth)
    {
        return postService.getPostById(auth.getName(), id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/create")
    public ResponseEntity<Post> createPost(
        @RequestBody CreatePostRequest request,
        Authentication auth)
    {
        Post post = postService.createPost(
            auth.getName(),
            request.getTitle(),
            request.getContent()
        );
        return ResponseEntity.ok(post);
    }
    
    @PutMapping("/update/{id}")
    public ResponseEntity<Post> updatePost(
        @PathVariable Long id,
        @RequestBody UpdatePostRequest request,
        Authentication auth)
    {
        if (!postService.isPostOwner(id, auth.getName()))
        {
            return ResponseEntity.status(403).build();
        }
        
        Post updated = postService.updatePost(
            id,
            request.getTitle(),
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
        private String title;
        private String content;
        
        public String getTitle()
        {
            return title;
        }
        
        public void setTitle(String title)
        {
            this.title = title;
        }
        
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
        private String title;
        private String content;
        
        public String getContent()
        {
            return content;
        }
        
        public void setContent(String content)
        {
            this.content = content;
        }
        
        public String getTitle()
        {
            return title;
        }
        
        public void setTitle(String title)
        {
            this.title = title;
        }
    }
}