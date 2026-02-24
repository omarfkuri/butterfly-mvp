package com.social.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.social.api.entity.User;
import com.social.api.service.UserService;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@RestController
@RequestMapping("/auth")
public class AuthController
{
    private final UserService userService;
    
    public AuthController(UserService userService)
    {
        this.userService = userService;
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> register(
        @RequestParam 
        @Size(min = 3, max = 48)
        @Pattern(regexp = "^[a-zA-Z0-9 ]+$")
        String name,

        @RequestParam 
        @Size(min = 3, max = 32)
        @Pattern(regexp = "^[a-z0-9._]+$")
        String username,
        
        @RequestParam 
        @Size(min = 4, max = 16)
        @Pattern(regexp = "^[a-zA-Z0-9#]+$")
        String password
    )
    {
        userService.createUser(name, username, password);
        return ResponseEntity.ok().build();
    }
}