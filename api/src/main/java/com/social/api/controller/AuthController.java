package com.social.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.social.api.entity.User;
import com.social.api.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController
{
    private final UserService userService;
    private final PasswordEncoder encoder;
    
    public AuthController(UserService userService, PasswordEncoder encoder)
    {
        this.userService = userService;
        this.encoder = encoder;
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> register(
        @RequestParam String username,
        @RequestParam String password)
    {
        if (userService.existsByUsername(username))
        {
            return ResponseEntity.status(409).body("User exists");
        }
        
        User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(password));
        userService.save(user);
        
        return ResponseEntity.ok().build();
    }
}