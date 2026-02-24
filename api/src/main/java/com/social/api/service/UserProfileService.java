package com.social.api.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.social.api.dto.UserProfileDto;
import com.social.api.entity.Image;
import com.social.api.entity.User;
import com.social.api.repository.ImageRepository;
import com.social.api.repository.UserRepository;

@Service
public class UserProfileService
{
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final FirebaseStorageService storageService;
    
    public UserProfileService(
        ImageRepository imageRepository,
        FirebaseStorageService storageService,
        UserRepository userRepository
    )
    {  
        this.imageRepository = imageRepository;
        this.storageService = storageService;
        this.userRepository = userRepository;
    }
    
    public void setProfilePicture(String username, MultipartFile file)
    {
        var user = userRepository.findByUsername(username)
        .orElseThrow(() -> new IllegalArgumentException(
          "Failed to get user for profile"
        ));

        var img = uploadImage(user, file);
        user.getProfile().setProfilePicture(img);

        userRepository.save(user);
    }
    
    public void setCoverPicture(String username, MultipartFile file)
    {
        var user = userRepository.findByUsername(username)
        .orElseThrow(() -> new IllegalArgumentException(
          "Failed to get user for profile"
        ));

        var img = uploadImage(user, file);
        user.getProfile().setCoverPicture(img);

        userRepository.save(user);
    }

    public UserProfileDto getUserProfile(String username)
    {
        var user = userRepository.findByUsername(username)
        .orElseThrow(() -> new IllegalArgumentException(
          "Failed to get user for profile"
        ));

        var prof = user.getProfile();

        var profPic = prof.getProfilePicture();
        var covrPic = prof.getCoverPicture();

        System.out.println("Profile: pic -> " + 
            (profPic == null ? null : profPic.getKey()) + 
            " cover -> " +
            (covrPic == null ? null : covrPic.getKey()));

        return new UserProfileDto(
            profPic != null ? storageService.getPublicUrl(profPic.getKey()) : null,
            covrPic != null ? storageService.getPublicUrl(covrPic.getKey()) : null
        );
    }

    private Image uploadImage(User user, MultipartFile file)
    {
        var key = storageService.uploadFile(file);

        var img = new Image();
        img.setKey(key);
        img.setUser(user);

        imageRepository.save(img);

        return img;
    }
}