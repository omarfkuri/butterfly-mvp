package com.social.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.social.api.entity.Post;
import com.social.api.service.PostService;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController
{
    private final PostService postService;
    
    public PostController(PostService postService)
    {
        this.postService = postService;
    }
    
    @GetMapping("/all")
    public List<Post> getAllPublicPosts()
    {
        return postService.getAllPosts();
    }
    
    @GetMapping("/user/{username}")
    public List<Post> getUserPublicPosts(@PathVariable String username)
    {
        return postService.getAllPostsByUsername(username);
    }
    
    @GetMapping("/get/{id}")
    public ResponseEntity<Post> getPublicPost(@PathVariable Long id)
    {
        return postService.getPostById(id)
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
        System.out.println("Hi there");
        if (!postService.isPostOwner(id, auth.getName()))
        {
            return ResponseEntity.status(403).build();
        }
        
        System.out.println("Wop!");
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