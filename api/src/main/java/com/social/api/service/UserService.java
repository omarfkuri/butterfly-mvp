package com.social.api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.social.api.dto.UserDto;
import com.social.api.entity.User;
import com.social.api.entity.UserProfile;
import com.social.api.repository.UserRepository;
import java.util.List;
import java.util.Optional;

@Service
public class UserService
{
    private final FirebaseStorageService storageService;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    
    public UserService(
        UserRepository userRepository,
        FirebaseStorageService storageService,
        PasswordEncoder encoder
    )
    {
        this.storageService = storageService;
        this.userRepository = userRepository;
        this.encoder = encoder;
    }
    
    public Optional<User> findByUsername(String username)
    {
        return userRepository.findByUsername(username);
    }
    
    public Optional<UserDto> findDtoByUsername(String username)
    {
        return userRepository.findDtoByUsername(username).map((UserDto u) -> {
            return new UserDto(
                u.id(),
                u.name(),
                u.username(),
                u.createdAt(),
                u.profilePictureURL() == null
                ? null
                : storageService.getPublicUrl(u.profilePictureURL()),
                u.coverPictureURL() == null
                ? null
                : storageService.getPublicUrl(u.coverPictureURL())
            );
        });
    }
    
    public Optional<User> findById(Long id)
    {
        return userRepository.findById(id);
    }
    
    public List<User> findAll()
    {
        return userRepository.findAll();
    }
    
    public void createUser(String name, String username, String password)
    {
        if (userRepository.existsByUsername(username))
        {
            throw new IllegalArgumentException("Username already exists");
        }
        
        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setPassword(encoder.encode(password));
        user.setProfile(new UserProfile());
        userRepository.save(user);
    }
    
    @Transactional
    public void deleteUser(Long id)
    {
        userRepository.deleteById(id);
    }
    
    @Transactional
    public void deleteByUsername(String username)
    {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }
}