package com.social.api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.social.api.entity.User;
import com.social.api.repository.UserRepository;
import java.util.List;
import java.util.Optional;

@Service
public class UserService
{
    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }
    
    public Optional<User> findByUsername(String username)
    {
        return userRepository.findByUsername(username);
    }
    
    public Optional<User> findById(Long id)
    {
        return userRepository.findById(id);
    }
    
    public User save(User user)
    {
        return userRepository.save(user);
    }
    
    public List<User> findAll()
    {
        return userRepository.findAll();
    }
    
    public boolean existsByUsername(String username)
    {
        return userRepository.existsByUsername(username);
    }
    
    @Transactional
    public void deleteUser(Long id)
    {
        // This will cascade delete all posts due to CascadeType.ALL
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